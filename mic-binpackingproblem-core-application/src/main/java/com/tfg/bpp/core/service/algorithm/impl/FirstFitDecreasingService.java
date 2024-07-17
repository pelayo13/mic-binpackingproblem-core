package com.tfg.bpp.core.service.algorithm.impl;

import com.tfg.bpp.core.mapper.BppStoredItemMapper;
import com.tfg.bpp.core.model.BppBin;
import com.tfg.bpp.core.model.BppDetailedSolution;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppItem;
import com.tfg.bpp.core.model.BppSolution;
import com.tfg.bpp.core.service.algorithm.AlgorithmService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirstFitDecreasingService implements AlgorithmService {

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

    recordInstances.add(bppInstance.copy());
    while (!this.isSolution(bppInstance)) {
      this.nextInstance(bppInstance, bppInstance.getItems().get(0), time);
      recordInstances.add(bppInstance.copy());
    }

    return BppDetailedSolution.builder().recordInstances(recordInstances).build();
  }

  private void nextInstance(BppInstance bppInstance, BppItem itemToStore, AtomicInteger time) {
    Optional<BppBin> binToStore =
        bppInstance.getBins().stream()
            .filter(bin -> bin.canBeStored(itemToStore, bppInstance.getBinsCapacity()))
            .findFirst();
    if (binToStore.isPresent()) {
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
