package com.tfg.bpp.core.service.operation.impl;

import com.tfg.bpp.core.mapper.BppStoredItemMapper;
import com.tfg.bpp.core.model.BppBin;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppItem;
import com.tfg.bpp.core.model.BppNeighborhoodStructureOperationType;
import com.tfg.bpp.core.model.BppStoredItem;
import com.tfg.bpp.core.model.BppStoredItemsAndFreeItemsInterchangeOperation;
import com.tfg.bpp.core.service.operation.NeighborhoodStructureOperationService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(BppNeighborhoodStructureOperationType.ServiceName.STORED_ITEMS_AND_FREE_ITEMS_INTERCHANGE)
@Slf4j
@RequiredArgsConstructor
public class StoredItemsAndFreeItemsInterchangeOperationService
    implements NeighborhoodStructureOperationService<
        BppStoredItemsAndFreeItemsInterchangeOperation> {

  private final BppStoredItemMapper bppStoredItemMapper;

  @Override
  public List<BppInstance> apply(
      List<BppInstance> instances, BppStoredItemsAndFreeItemsInterchangeOperation operation) {
    List<BppInstance> resultInstances = new ArrayList<>();

    instances.forEach(
        instance -> {
          Stream<List<BppItem>> combinationsFreeItemsToInterchange =
              this.combinations(instance.getItems(), operation.getNumberItemsToInterchangeFrom());

          combinationsFreeItemsToInterchange.forEach(
              freeItemsFrom ->
                  IntStream.range(0, instance.getBins().size())
                      .forEach(
                          binIndexTo ->
                              this.combinations(
                                      instance.getBins().get(binIndexTo).getItems(),
                                      operation.getNumberItemsToInterchangeTo())
                                  .forEach(
                                      storedItemsTo -> {
                                        if (this.canBeStored(
                                                freeItemsFrom,
                                                storedItemsTo,
                                                instance.getBins().get(binIndexTo),
                                                instance.getBinsCapacity())
                                            && operation
                                                .getItemsInterchangeFunction()
                                                .isImproved(freeItemsFrom, storedItemsTo)) {
                                          BppInstance newNeighbor = new BppInstance(instance);

                                          newNeighbor
                                              .getBins()
                                              .get(binIndexTo)
                                              .removeItems(storedItemsTo);
                                          newNeighbor.getItems().removeAll(freeItemsFrom);
                                          newNeighbor
                                              .getBins()
                                              .get(binIndexTo)
                                              .addItems(
                                                  freeItemsFrom.stream()
                                                      .map(
                                                          item ->
                                                              this.bppStoredItemMapper
                                                                  .toBppStoredItem(
                                                                      item,
                                                                      newNeighbor
                                                                          .getBins()
                                                                          .get(binIndexTo)
                                                                          .getFirstUseInstant()))
                                                      .toList());
                                          newNeighbor
                                              .getItems()
                                              .addAll(
                                                  storedItemsTo.stream()
                                                      .map(this.bppStoredItemMapper::toBppItem)
                                                      .toList());

                                          resultInstances.add(new BppInstance(newNeighbor));
                                        }
                                      })));
        });

    return resultInstances;
  }

  private boolean canBeStored(
      List<BppItem> itemsFromToInterchange,
      List<BppStoredItem> itemsToToInterchange,
      BppBin binToStore,
      int binsCapacity) {
    int sizeOfFrom = itemsFromToInterchange.stream().mapToInt(BppItem::getSize).sum();
    int sizeOfTo = itemsToToInterchange.stream().mapToInt(BppItem::getSize).sum();

    return binsCapacity >= binToStore.getOccupiedCapacity() - sizeOfTo + sizeOfFrom;
  }

  private <T> Stream<List<T>> combinations(List<T> items, int size) {
    if (size == 0) {
      return Stream.of(Collections.emptyList());
    } else {
      return IntStream.range(0, items.size())
          .boxed()
          .flatMap(
              i ->
                  combinations(items.subList(i + 1, items.size()), size - 1)
                      .map(t -> pipe(items.get(i), t)));
    }
  }

  private <T> List<T> pipe(T head, List<T> tail) {
    List<T> newList = new ArrayList<>(tail);
    newList.add(0, head);
    return newList;
  }
}
