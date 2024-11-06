package com.tfg.bpp.core.service.operation.impl;

import com.tfg.bpp.core.model.BppBin;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppItem;
import com.tfg.bpp.core.model.BppNeighborhoodStructureOperationType;
import com.tfg.bpp.core.model.BppStoredItem;
import com.tfg.bpp.core.model.BppStoredItemsInterchangeOperation;
import com.tfg.bpp.core.service.operation.NeighborhoodStructureOperationService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(BppNeighborhoodStructureOperationType.ServiceName.STORED_ITEMS_INTERCHANGE)
@Slf4j
@RequiredArgsConstructor
public class StoredItemsInterchangeOperationService
    implements NeighborhoodStructureOperationService<BppStoredItemsInterchangeOperation> {

  private static int getStartInclusive(
      BppStoredItemsInterchangeOperation operation, int indexFirstBinToInterchange) {
    return operation.getNumberItemsToInterchangeTo() == operation.getNumberItemsToInterchangeFrom()
        ? indexFirstBinToInterchange + 1
        : 0;
  }

  @Override
  public List<BppInstance> apply(
      List<BppInstance> instances, BppStoredItemsInterchangeOperation operation) {
    List<BppInstance> resultInstances = new ArrayList<>();

    instances.forEach(
        instance -> {
          IntStream.range(0, instance.getBins().size())
              .forEach(
                  indexFrom -> {
                    BppBin binFrom = instance.getBins().get(indexFrom);
                    List<List<BppStoredItem>> combinationsFrom =
                        this.combinations(
                                binFrom.getItems(), operation.getNumberItemsToInterchangeFrom())
                            .toList();

                    combinationsFrom.forEach(
                        combinationFrom ->
                            IntStream.range(
                                    getStartInclusive(operation, indexFrom),
                                    instance.getBins().size())
                                .forEach(
                                    indexTo -> {
                                      if (indexFrom == indexTo) {
                                        return;
                                      }

                                      BppBin binTo = instance.getBins().get(indexTo);
                                      List<List<BppStoredItem>> combinationsTo =
                                          this.combinations(
                                                  instance.getBins().get(indexTo).getItems(),
                                                  operation.getNumberItemsToInterchangeTo())
                                              .toList();

                                      combinationsTo.forEach(
                                          combinationTo -> {
                                            if (this.canBeInterchanged(
                                                    combinationFrom,
                                                    combinationTo,
                                                    binFrom,
                                                    binTo,
                                                    instance.getBinsCapacity())
                                                && operation
                                                    .getItemsInterchangeFunction()
                                                    .isImproved(combinationFrom, combinationTo)) {
                                              BppInstance newNeighbor = new BppInstance(instance);

                                              newNeighbor
                                                  .getBins()
                                                  .get(indexFrom)
                                                  .removeItems(combinationFrom);
                                              newNeighbor
                                                  .getBins()
                                                  .get(indexTo)
                                                  .removeItems(combinationTo);
                                              newNeighbor
                                                  .getBins()
                                                  .get(indexFrom)
                                                  .addItems(
                                                      combinationTo.stream()
                                                          .map(BppStoredItem::new)
                                                          .peek(
                                                              item ->
                                                                  item.setTimeMetrics(indexFrom))
                                                          .collect(Collectors.toList()));
                                              newNeighbor
                                                  .getBins()
                                                  .get(indexTo)
                                                  .addItems(
                                                      combinationFrom.stream()
                                                          .map(BppStoredItem::new)
                                                          .peek(
                                                              item -> item.setTimeMetrics(indexTo))
                                                          .collect(Collectors.toList()));

                                              resultInstances.add(newNeighbor);
                                            }
                                          });
                                    }));
                  });
        });

    return resultInstances;
  }

  private boolean canBeInterchanged(
      List<BppStoredItem> itemsFromToInterchange,
      List<BppStoredItem> itemsToToInterchange,
      BppBin binFromToStore,
      BppBin binToToStore,
      int binsCapacity) {
    int sizeOfFrom = itemsFromToInterchange.stream().mapToInt(BppItem::getSize).sum();
    int sizeOfTo = itemsToToInterchange.stream().mapToInt(BppItem::getSize).sum();

    return binsCapacity >= binToToStore.getOccupiedCapacity() - sizeOfTo + sizeOfFrom
        && binsCapacity >= binFromToStore.getOccupiedCapacity() - sizeOfFrom + sizeOfTo;
  }

  private Stream<List<BppStoredItem>> combinations(List<BppStoredItem> items, int size) {
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

  private List<BppStoredItem> pipe(BppStoredItem head, List<BppStoredItem> tail) {
    List<BppStoredItem> newList = new ArrayList<>(tail);
    newList.add(0, head);
    return newList;
  }
}
