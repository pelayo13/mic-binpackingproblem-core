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
public class BppTestResults {

  private double averageNumberBins;

  private double averageTardiness;

  private double averageSeconds;

  private BppAlgorithm algorithm;

  private List<BppTestCaseResults> testCaseResults;
}
