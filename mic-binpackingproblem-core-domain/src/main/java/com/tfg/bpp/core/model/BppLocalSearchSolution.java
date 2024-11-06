package com.tfg.bpp.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BppLocalSearchSolution extends BppInstance {

  private int numberIterations;

  private int numberNeighborsGenerated;

  private Integer numberEvaluatedNeighbors;

  private List<Double> evaluationFunctionResultsRecords;
}
