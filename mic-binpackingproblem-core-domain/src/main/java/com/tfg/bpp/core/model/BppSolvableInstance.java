package com.tfg.bpp.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BppSolvableInstance {

  private BppGreedyAlgorithmType greedyAlgorithmType;

  private BppLocalSearchType localSearchType;

  private BppInstance instance;
}
