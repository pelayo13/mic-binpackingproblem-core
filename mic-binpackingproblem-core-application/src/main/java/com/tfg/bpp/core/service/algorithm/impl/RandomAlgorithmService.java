package com.tfg.bpp.core.service.algorithm.impl;

import com.tfg.bpp.core.mapper.BppStoredItemMapper;
import com.tfg.bpp.core.model.BppBin;
import com.tfg.bpp.core.model.BppDetailedSolution;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppItem;
import com.tfg.bpp.core.service.algorithm.AlgorithmService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RandomAlgorithmService implements AlgorithmService {

  private final BppStoredItemMapper bppStoredItemMapper;

  private final Random random = new Random();

  @Override
  public BppInstance getSolution(BppInstance bppInstance) {
    AtomicInteger time = new AtomicInteger(0);

    while (!this.isSolution(bppInstance)) {
      int itemIndex = this.random.nextInt(bppInstance.getItems().size());
      BppItem itemToStore = bppInstance.getItems().get(itemIndex);
      this.nextInstance(bppInstance, itemToStore, time);
    }

    return bppInstance;
  }

  @Override
  public BppDetailedSolution getDetailedSolution(BppInstance bppInstance) {
    AtomicInteger time = new AtomicInteger(0);
    List<BppInstance> recordInstances = new ArrayList<>();

    recordInstances.add(bppInstance);
    while (!this.isSolution(bppInstance)) {
      int itemIndex = this.random.nextInt(bppInstance.getItems().size());
      BppItem itemToStore = bppInstance.getItems().get(itemIndex);
      this.nextInstance(bppInstance, itemToStore, time);
      recordInstances.add(bppInstance);
    }

    return BppDetailedSolution.builder().recordInstances(recordInstances).build();
  }

  private boolean isSolution(BppInstance bppInstance) {
    return bppInstance.getItems().isEmpty();
  }

  private void nextInstance(BppInstance bppInstance, BppItem itemToStore, AtomicInteger time) {
    int binIndexToStore = 0;
    if (ObjectUtils.isNotEmpty(bppInstance.getBins())) {
      binIndexToStore = this.random.nextInt(bppInstance.getBins().size() + 1);
    }

    if (this.isIndexInBounds(bppInstance.getBins(), binIndexToStore)) {
      BppBin binToStore = bppInstance.getBins().get(binIndexToStore);
      while (this.isIndexInBounds(bppInstance.getBins(), binIndexToStore)
          && !binToStore.canBeStored(itemToStore, bppInstance.getBinsCapacity())) {
        binIndexToStore = this.random.nextInt(bppInstance.getBins().size() + 1);
      }
      if (this.isIndexInBounds(bppInstance.getBins(), binIndexToStore)) {
        bppInstance.addStoredItemByBinIndex(
            this.bppStoredItemMapper.toBppStoredItem(itemToStore, time.intValue()),
            binIndexToStore);
      } else {
        this.addNewBin(bppInstance, time, itemToStore);
      }
    } else {
      this.addNewBin(bppInstance, time, itemToStore);
    }

    bppInstance.getItems().remove(itemToStore);
  }

  private boolean isIndexInBounds(List<?> list, int binIndexToStore) {
    return binIndexToStore < list.size();
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
}
