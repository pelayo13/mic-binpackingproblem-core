package com.tfg.bpp.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BppLocalSearchAlgorithm {

  private List<BppNeighborhoodStructure> bppNeighborhoodStructures;

  private BppStrategyControl bppStrategyControl;

  private BppEvaluationFunction bppEvaluationFunction;

  private BppStoppingCriteria bppStopCriteria;
}
