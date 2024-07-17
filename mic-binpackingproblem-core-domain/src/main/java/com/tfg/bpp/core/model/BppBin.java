package com.tfg.bpp.core.model;

import java.util.List;
import java.util.Optional;
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

  public boolean isFull(int capacity) {
    return this.getOccupiedCapacity() > capacity;
  }

  public int getOccupiedCapacity() {
    return Optional.ofNullable(items)
        .map(items -> items.stream().mapToInt(BppItem::getSize).sum())
        .orElse(0);
  }

  public int getAvailableCapacity(int capacity) {
    return Optional.ofNullable(items)
            .map(items -> capacity - items.stream().mapToInt(BppItem::getSize).sum())
            .orElse(capacity);
  }

  public boolean canBeStored(BppItem itemToStore, int capacity) {
    return Optional.ofNullable(itemToStore)
            .map(item -> this.getOccupiedCapacity() + item.getSize() <= capacity)
            .orElse(true);
  }
}
