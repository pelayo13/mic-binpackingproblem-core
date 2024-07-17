package com.tfg.bpp.core.service.algorithm.impl;

import com.tfg.bpp.core.mapper.BppStoredItemMapper;
import com.tfg.bpp.core.model.BppBin;
import com.tfg.bpp.core.model.BppDetailedSolution;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppItem;
import com.tfg.bpp.core.service.algorithm.AlgorithmService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BestFitDecreasingService implements AlgorithmService {

  private final BppStoredItemMapper bppStoredItemMapper;

  @Override
  public BppInstance getSolution(BppInstance bppInstance) {
    AtomicInteger time = new AtomicInteger(0);
    bppInstance.getItems().sort((item1, item2) -> item2.getSize() - item1.getSize());

    while (!this.isSolution(bppInstance)) {
      this.nextInstance(bppInstance, bppInstance.getItems().get(0), time);
    }

    return bppInstance;
  }

  @Override
  public BppDetailedSolution getDetailedSolution(BppInstance bppInstance) {
    AtomicInteger time = new AtomicInteger(0);
    bppInstance.getItems().sort((item1, item2) -> item2.getSize() - item1.getSize());
    List<BppInstance> recordInstances = new ArrayList<>();

    recordInstances.add(bppInstance);
    while (!this.isSolution(bppInstance)) {
      this.nextInstance(bppInstance, bppInstance.getItems().get(0), time);
      recordInstances.add(bppInstance);
    }

    return BppDetailedSolution.builder().recordInstances(recordInstances).build();
  }

  private void nextInstance(BppInstance bppInstance, BppItem itemToStore, AtomicInteger time) {
    AtomicInteger bestFitCapacity = new AtomicInteger(bppInstance.getBinsCapacity());
    AtomicReference<BppBin> binToStore = new AtomicReference<>();

    bppInstance
        .getBins()
        .forEach(
            bin -> {
              if (bin.canBeStored(itemToStore, bppInstance.getBinsCapacity())
                  && bin.getAvailableCapacity(bppInstance.getBinsCapacity()) - itemToStore.getSize()
                      < bestFitCapacity.intValue()) {
                bestFitCapacity.set(
                    bin.getAvailableCapacity(bppInstance.getBinsCapacity())
                        - itemToStore.getSize());
                binToStore.set(bin);
              }
            });
    if (!ObjectUtils.isEmpty(binToStore.get())) {
      binToStore
          .get()
          .getItems()
          .add(this.bppStoredItemMapper.toBppStoredItem(itemToStore, time.intValue()));
    } else {
      this.addNewBin(bppInstance, time, itemToStore);
    }

    bppInstance.getItems().remove(itemToStore);
  }

  private void addNewBin(BppInstance bppInstance, AtomicInteger time, BppItem itemToStore) {
    bppInstance
        .getBins()
        .add(
            BppBin.builder()
                .items(
                    new ArrayList<>(
                        List.of(
                            this.bppStoredItemMapper.toBppStoredItem(
                                itemToStore, time.intValue()))))
                .build());
    time.incrementAndGet();
  }

  private boolean isSolution(BppInstance bppInstance) {
    return bppInstance.getItems().isEmpty();
  }
}
