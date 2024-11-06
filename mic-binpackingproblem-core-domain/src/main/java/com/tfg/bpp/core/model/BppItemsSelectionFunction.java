package com.tfg.bpp.core.model;

import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BppItemsSelectionFunction {
  BIGGEST_SIZE() {
    @Override
    public Stream<List<BppItem>> sort(Stream<List<BppItem>> items) {
      return items.sorted(
          (items1, items2) ->
              items2.stream().mapToInt(BppItem::getSize).sum()
                  - items1.stream().mapToInt(BppItem::getSize).sum());
    }
  };

  public abstract Stream<List<BppItem>> sort(Stream<List<BppItem>> items);
}
