package com.tfg.bpp.core.model;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public enum BppStrategyControl {
  HILL_CLIMBING {
    @Override
    public BppStrategyControlResult getNewSolution(
        BppInstance currentSolution,
        List<BppInstance> neighbors,
        BppEvaluationFunction evaluationFunction) {
      return IntStream.range(0, neighbors.size())
          .filter(
              index ->
                  evaluationFunction.getValue(neighbors.get(index))
                      < evaluationFunction.getValue(currentSolution))
          .mapToObj(
              index ->
                  BppStrategyControlResult.builder()
                      .newSolution(neighbors.get(index))
                      .numberEvaluatedNeighbors(index + 1)
                      .build())
          .findFirst()
          .orElse(
              BppStrategyControlResult.builder()
                  .newSolution(currentSolution)
                  .numberEvaluatedNeighbors(neighbors.size())
                  .build());
    }
  },
  GRADIENT_DESCENT() {
    @Override
    public BppStrategyControlResult getNewSolution(
        BppInstance currentSolution,
        List<BppInstance> neighbors,
        BppEvaluationFunction evaluationFunction) {
      Optional<BppInstance> newSolution =
          neighbors.stream().min(Comparator.comparing(evaluationFunction::getValue));

      if (newSolution.isPresent()
          && evaluationFunction.getValue(newSolution.get())
              < evaluationFunction.getValue(currentSolution)) {
        return BppStrategyControlResult.builder()
            .newSolution(newSolution.get())
            .numberEvaluatedNeighbors(neighbors.size())
            .build();
      } else {
        return BppStrategyControlResult.builder()
            .newSolution(currentSolution)
            .numberEvaluatedNeighbors(neighbors.size())
            .build();
      }
    }
  };

  public abstract BppStrategyControlResult getNewSolution(
      BppInstance currentSolution,
      List<BppInstance> neighbors,
      BppEvaluationFunction evaluationFunction);
}
