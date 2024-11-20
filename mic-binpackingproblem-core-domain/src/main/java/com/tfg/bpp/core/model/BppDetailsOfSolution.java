package com.tfg.bpp.core.model;

import java.util.ArrayList;
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
public class BppDetailsOfSolution {

  private List<BppInstance> recordInstances;

  public BppDetailsOfSolution(BppDetailsOfSolution detailsOfSolution) {
    this.recordInstances =
        Optional.ofNullable(detailsOfSolution.getRecordInstances())
            .map(ArrayList::new)
            .orElse(null);
  }
}
