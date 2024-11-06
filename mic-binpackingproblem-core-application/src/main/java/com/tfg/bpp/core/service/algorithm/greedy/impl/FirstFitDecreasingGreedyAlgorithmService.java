package com.tfg.bpp.core.service.algorithm.greedy.impl;

import com.tfg.bpp.core.mapper.BppStoredItemMapper;
import com.tfg.bpp.core.model.BppBin;
import com.tfg.bpp.core.model.BppDetailedSolution;
import com.tfg.bpp.core.model.BppGreedyAlgorithmType;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tfg.bpp.core.service.algorithm.greedy.GreedyAlgorithmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(BppGreedyAlgorithmType.ServiceName.FIRST_FIT_DECREASING)
@Slf4j
@RequiredArgsConstructor
public class FirstFitDecreasingGreedyAlgorithmService implements GreedyAlgorithmService {

  private final BppStoredItemMapper bppStoredItemMapper;

  @Override
  public BppInstance getSolution(BppInstance bppInstance) {
    bppInstance.getItems().sort((item1, item2) -> item2.getSize() - item1.getSize());

    while (!this.isSolution(bppInstance)) {
      this.nextInstance(bppInstance, bppInstance.getItems().get(0));
    }

    return bppInstance;
  }

  @Override
  public BppDetailedSolution getDetailedSolution(BppInstance bppInstance) {
    bppInstance.getItems().sort((item1, item2) -> item2.getSize() - item1.getSize());
    List<BppInstance> recordInstances = new ArrayList<>();

    recordInstances.add(new BppInstance(bppInstance));
    while (!this.isSolution(bppInstance)) {
      this.nextInstance(bppInstance, bppInstance.getItems().get(0));
      recordInstances.add(new BppInstance(bppInstance));
    }

    return BppDetailedSolution.builder().recordInstances(recordInstances).build();
  }

  private void nextInstance(BppInstance bppInstance, BppItem itemToStore) {
    Optional<BppBin> binToStore =
        bppInstance.getBins().stream()
            .filter(bin -> bin.canBeStored(List.of(itemToStore), bppInstance.getBinsCapacity()))
            .findFirst();
    if (binToStore.isPresent()) {
      binToStore
          .get()
          .addItem(this.bppStoredItemMapper.toBppStoredItem(itemToStore, binToStore.get().getFirstUseInstant()));
      binToStore.get().recalculateMaximumLateness();
    } else {
      this.addNewBin(bppInstance, itemToStore);
    }

    bppInstance.getItems().remove(itemToStore);
  }

  private void addNewBin(BppInstance bppInstance, BppItem itemToStore) {
    BppBin newBin = BppBin.builder()
            .items(
                    new ArrayList<>(
                            List.of(
                                    this.bppStoredItemMapper.toBppStoredItem(
                                            itemToStore, bppInstance.getBins().size()))))
            .occupiedCapacity(itemToStore.getSize())
            .build();
    newBin.setFirstUseInstant(bppInstance.getBins().size());

    bppInstance
            .getBins()
            .add(newBin);
  }

  private boolean isSolution(BppInstance bppInstance) {
    return bppInstance.getItems().isEmpty();
  }
}
