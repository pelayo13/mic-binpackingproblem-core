package com.tfg.bpp.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BppTestCaseMetrics {

  private int numberBins;

  private double averageTardinessStoredItems;

  private double averageLatenessStoredItems;

  private double maximumLateness;

  private int availableCapacity;

  private Integer numberIterations;

  private Integer numberNeighborsGenerated;

  private Integer numberEvaluatedNeighbors;

  private double fitness;

  private List<Double> evaluationFunctionResultsRecords;

  private double seconds;
}
