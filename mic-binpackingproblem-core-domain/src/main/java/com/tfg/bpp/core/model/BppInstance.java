package com.tfg.bpp.core.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BppInstance {

  private List<BppItem> items;

  private List<BppBin> bins;

  private int binsCapacity;

  public void addStoredItemByBinIndex(BppStoredItem bppStoredItem, int binIndexToStore) {
    if (null != bppStoredItem && binIndexToStore >= 0 && binIndexToStore < this.getBins().size()) {
      this.getBins().get(binIndexToStore).getItems().add(bppStoredItem);
    }
  }

  public BppInstance copy() {
    return BppInstance.builder()
        .bins(new ArrayList<>(this.getBins()))
        .items(new ArrayList<>(this.getItems()))
        .binsCapacity(this.getBinsCapacity())
        .build();
  }
}
