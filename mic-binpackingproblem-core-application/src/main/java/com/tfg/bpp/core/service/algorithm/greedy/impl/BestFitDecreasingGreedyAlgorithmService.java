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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service(BppGreedyAlgorithmType.ServiceName.BEST_FIT_DECREASING)
@RequiredArgsConstructor
public class BestFitDecreasingGreedyAlgorithmService implements GreedyAlgorithmService {

  private final BppStoredItemMapper bppStoredItemMapper;

  @Override
  public BppInstance getSolution(BppInstance instance) {
    BppInstance bppInstance = new BppInstance(instance);
    bppInstance.getItems().sort((item1, item2) -> item2.getSize() - item1.getSize());
    List<BppInstance> recordInstances =
        Optional.ofNullable(bppInstance.getDetails())
            .map(BppDetailsOfSolution::getRecordInstances)
            .orElse(new ArrayList<>());

    recordInstances.add(new BppInstance(bppInstance));
    while (!this.isSolution(bppInstance)) {
      this.nextInstance(bppInstance, bppInstance.getItems().get(0));
      recordInstances.add(new BppInstance(bppInstance));
    }
    bppInstance.setDetails(BppDetailsOfSolution.builder().recordInstances(recordInstances).build());

    return bppInstance;
  }

  private void nextInstance(BppInstance bppInstance, BppItem itemToStore) {
    AtomicInteger bestFitCapacity = new AtomicInteger(bppInstance.getBinsCapacity());
    AtomicReference<BppBin> binToStore = new AtomicReference<>();

    bppInstance
        .getBins()
        .forEach(
            bin -> {
              if (bin.canBeStored(List.of(itemToStore), bppInstance.getBinsCapacity())
                  && bin.getAvailableCapacity(bppInstance.getBinsCapacity()) - itemToStore.getSize()
                      <= bestFitCapacity.intValue()) {
                bestFitCapacity.set(
                    bin.getAvailableCapacity(bppInstance.getBinsCapacity())
                        - itemToStore.getSize());
                binToStore.set(bin);
              }
            });
    if (!ObjectUtils.isEmpty(binToStore.get())) {
      binToStore
          .get()
          .addItem(
              this.bppStoredItemMapper.toBppStoredItem(
                  itemToStore, binToStore.get().getFirstUseInstant()));
    } else {
      this.addNewBin(bppInstance, itemToStore);
    }

    bppInstance.getItems().remove(itemToStore);
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
