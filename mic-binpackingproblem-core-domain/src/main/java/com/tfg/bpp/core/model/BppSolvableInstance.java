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

  private BppAlgorithm algorithm;

  private BppInstance instance;

  public BppSolvableInstance(BppSolvableInstance solvableInstance) {
    this.algorithm = solvableInstance.getAlgorithm();
    this.instance = new BppInstance(solvableInstance.getInstance());
  }
}
