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
public class BppAlgorithmMetrics {

  private double averageNumberBins;

  private double averageTardiness;

  private double averageLateness;

  private double averageMaximumLateness;

  private double averageAvailableCapacity;

  private Double averageNumberIterations;

  private Double averageNumberNeighborsGenerated;

  private Double averageNumberEvaluatedNeighbors;

  private double standardDeviationFitness;

  private double averageFitness;

  private BppTestCaseMetrics bestCaseByFitness;

  private double averageSeconds;

  private BppAlgorithm algorithm;

  private List<BppTestCaseMetrics> testCasesMetrics;
}
