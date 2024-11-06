package com.tfg.bpp.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BppStrategyControlResult {

  private BppInstance newSolution;

  private Integer numberEvaluatedNeighbors;
}
