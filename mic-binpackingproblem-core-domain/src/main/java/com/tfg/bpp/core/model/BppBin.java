package com.tfg.bpp.core.model;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BppBin {

  private List<BppStoredItem> items;

  private int occupiedCapacity = 0;

  private Integer firstUseInstant;

  private Integer maximumLateness;

  public BppBin(BppBin bin) {
    this.items = bin.getItems().stream().map(BppStoredItem::new).collect(Collectors.toList());
    this.occupiedCapacity = bin.getOccupiedCapacity();
    this.firstUseInstant = bin.getFirstUseInstant();
    this.maximumLateness = bin.getMaximumLateness();
  }

  public void setFirstUseInstant(Integer indexInBinsOfInstance) {
    this.firstUseInstant = this.getFirstUseInstant(indexInBinsOfInstance);
    items.forEach(item -> item.setTimeMetrics(firstUseInstant));
    recalculateMaximumLateness();
  }

  private Integer getFirstUseInstant(Integer indexInBinsOfInstance) {
    return indexInBinsOfInstance + 1;
  }

  public void recalculateMaximumLateness() {
    OptionalInt maximumLateness =
        this.getItems().stream().mapToInt(BppStoredItem::getLateness).max();
    this.setMaximumLateness(maximumLateness.isPresent() ? maximumLateness.getAsInt() : null);
  }

  public int getAvailableCapacity(int capacity) {
    return Optional.ofNullable(items)
        .map(items -> capacity - this.getOccupiedCapacity())
        .orElse(capacity);
  }

  public boolean canBeStored(List<? extends BppItem> itemsToStore, int capacity) {
    return Optional.ofNullable(itemsToStore)
        .map(items -> this.getOccupiedCapacity() + this.getTotalSize(items) <= capacity)
        .orElse(true);
  }

  private int getTotalSize(List<? extends BppItem> items) {
    return items.stream().mapToInt(BppItem::getSize).sum();
  }

  public void addItem(BppStoredItem bppStoredItem) {
    Optional.ofNullable(bppStoredItem)
        .ifPresent(
            item -> {
              this.setOccupiedCapacity(this.getOccupiedCapacity() + item.getSize());
              this.getItems().add(bppStoredItem);
            });
    recalculateMaximumLateness();
  }

  public void addItems(List<BppStoredItem> bppStoredItems) {
    bppStoredItems.forEach(this::addItem);
  }

  public void removeItem(BppStoredItem bppStoredItem) {
    Optional.ofNullable(bppStoredItem)
        .ifPresent(
            item -> {
              this.setOccupiedCapacity(this.getOccupiedCapacity() - item.getSize());
              this.getItems().remove(bppStoredItem);
            });
    recalculateMaximumLateness();
  }

  public void removeItems(List<BppStoredItem> bppStoredItems) {
    bppStoredItems.forEach(this::removeItem);
  }

  public void recalculateOccupiedCapacity() {
    this.setOccupiedCapacity(this.getTotalSize(this.getItems()));
  }
}
