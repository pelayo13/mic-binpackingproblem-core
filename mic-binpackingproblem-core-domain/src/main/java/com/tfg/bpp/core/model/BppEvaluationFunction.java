package com.tfg.bpp.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BppEvaluationFunction {
  BINS_NUMBER() {
    @Override
    public double getValue(BppInstance instance) {
      return instance.getBins().size();
    }
  },
  TARDINESS() {
    @Override
    public double getValue(BppInstance instance) {
      return instance.getTardiness();
    }
  },
  LATENESS() {
    @Override
    public double getValue(BppInstance instance) {
      return instance.getLateness();
    }
  },
  MAXIMUM_LATENESS() {
    @Override
    public double getValue(BppInstance instance) {
      return instance.getMaximumLateness();
    }
  },
  AVAILABLE_CAPACITY() {
    @Override
    public double getValue(BppInstance instance) {
      return instance.getAvailableCapacity();
    }
  },
  FITNESS() {
    @Override
    public double getValue(BppInstance instance) {
      return instance.getFitness();
    }
  };

  public abstract double getValue(BppInstance instance);
}
