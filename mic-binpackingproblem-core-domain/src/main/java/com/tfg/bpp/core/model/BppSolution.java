package com.tfg.bpp.core.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BppSolution {

  private List<BppBin> bins;

  public double getAverageTardinessStoredItems() {
    return this.getBins().stream()
        .flatMapToInt(bppBin -> bppBin.getItems().stream().mapToInt(BppStoredItem::getTardiness))
        .average()
        .orElse(0);
  }
}
