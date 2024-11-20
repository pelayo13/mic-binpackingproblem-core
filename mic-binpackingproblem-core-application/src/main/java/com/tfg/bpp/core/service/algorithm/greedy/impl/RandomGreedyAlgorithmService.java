package com.tfg.bpp.core.service.algorithm.greedy.impl;

import com.tfg.bpp.core.mapper.BppStoredItemMapper;
import com.tfg.bpp.core.model.BppBin;
import com.tfg.bpp.core.model.BppDetailsOfSolution;
import com.tfg.bpp.core.model.BppGreedyAlgorithmType;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppItem;
import com.tfg.bpp.core.service.algorithm.greedy.GreedyAlgorithmService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service(BppGreedyAlgorithmType.ServiceName.RANDOM)
@Slf4j
@RequiredArgsConstructor
public class RandomGreedyAlgorithmService implements GreedyAlgorithmService {

  private final Random random = new Random();

  private final BppStoredItemMapper bppStoredItemMapper;

  @Override
  public BppInstance getSolution(BppInstance bppInstance) {
    List<BppInstance> recordInstances =
        Optional.ofNullable(bppInstance.getDetails())
            .map(BppDetailsOfSolution::getRecordInstances)
            .orElse(new ArrayList<>());

    recordInstances.add(new BppInstance(bppInstance));
    while (!this.isSolution(bppInstance)) {
      int itemIndex = this.random.nextInt(bppInstance.getItems().size());
      BppItem itemToStore = bppInstance.getItems().get(itemIndex);
      this.nextInstance(bppInstance, itemToStore);
      recordInstances.add(new BppInstance(bppInstance));
    }
    bppInstance.setDetails(BppDetailsOfSolution.builder().recordInstances(recordInstances).build());

    return bppInstance;
  }

  private void nextInstance(BppInstance bppInstance, BppItem itemToStore) {
    int binIndexToStore = 0;
    if (ObjectUtils.isNotEmpty(bppInstance.getBins())) {
      binIndexToStore = this.random.nextInt(bppInstance.getBins().size() + 1);
    }

    if (this.isIndexInBounds(bppInstance.getBins(), binIndexToStore)) {
      BppBin binToStore = bppInstance.getBins().get(binIndexToStore);
      while (this.isIndexInBounds(bppInstance.getBins(), binIndexToStore)
          && !binToStore.canBeStored(List.of(itemToStore), bppInstance.getBinsCapacity())) {
        binIndexToStore = this.random.nextInt(bppInstance.getBins().size() + 1);
      }
      if (this.isIndexInBounds(bppInstance.getBins(), binIndexToStore)) {
        bppInstance
            .getBins()
            .get(binIndexToStore)
            .addItem(
                this.bppStoredItemMapper.toBppStoredItem(
                    itemToStore, bppInstance.getBins().get(binIndexToStore).getFirstUseInstant()));
        bppInstance.getBins().get(binIndexToStore).recalculateMaximumLateness();
      } else {
        this.addNewBin(bppInstance, itemToStore);
      }
    } else {
      this.addNewBin(bppInstance, itemToStore);
    }

    bppInstance.getItems().remove(itemToStore);
  }

  private boolean isIndexInBounds(List<?> list, int binIndexToStore) {
    return binIndexToStore < list.size();
  }

  private void addNewBin(BppInstance bppInstance, BppItem itemToStore) {
    BppBin newBin =
        BppBin.builder()
            .items(
                new ArrayList<>(
                    List.of(
                        this.bppStoredItemMapper.toBppStoredItem(
                            itemToStore, bppInstance.getBins().size()))))
            .occupiedCapacity(itemToStore.getSize())
            .build();
    newBin.setFirstUseInstant(bppInstance.getBins().size());

    bppInstance.getBins().add(newBin);
  }

  private boolean isSolution(BppInstance bppInstance) {
    return bppInstance.getItems().isEmpty();
  }
}
