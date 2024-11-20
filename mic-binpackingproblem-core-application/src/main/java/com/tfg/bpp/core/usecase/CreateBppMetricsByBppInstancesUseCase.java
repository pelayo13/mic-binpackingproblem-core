package com.tfg.bpp.core.usecase;

import com.tfg.bpp.core.model.BppAlgorithmMetrics;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppLocalSearchSolution;
import com.tfg.bpp.core.model.BppTestCaseMetrics;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppMetricsByBppInstancesUseCasePort;
import com.tfg.bpp.core.service.algorithm.AlgorithmService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateBppMetricsByBppInstancesUseCase
    implements CreateBppMetricsByBppInstancesUseCasePort {

  public static final int NUMBER_REPETITIONS = 1;
  private static final String CLASS_NAME = CreateBppMetricsByBppInstancesUseCase.class.getName();

  private final AlgorithmService algorithmService;

  @Override
  public CreateBppMetricsByBppInstancesResponse execute(
      CreateBppMetricsByBppInstancesCommand command) {
    List<BppTestCaseMetrics> testCaseResults = new ArrayList<>();
    IntStream.range(0, this.getNumberRepetitions(command.getNumberRepetitions()))
        .forEach(
            repetition -> {
              StopWatch stopWatch = new StopWatch();
              log.info(
                  "[start] {}.execute - instance: {}", CLASS_NAME, command.getSolvableInstance());
              stopWatch.start();
              BppInstance solution =
                  this.algorithmService.getSolution(
                      command.getSolvableInstance().getInstance(),
                      command.getSolvableInstance().getAlgorithm().getGreedyAlgorithmType());

              BppLocalSearchSolution response =
                  this.algorithmService.getLocalSearchSolution(
                      solution,
                      command.getSolvableInstance().getAlgorithm().getLocalSearchAlgorithm());
              stopWatch.stop();
              log.info(
                  "[start] {}.execute - instance: {}", CLASS_NAME, command.getSolvableInstance());

              testCaseResults.add(
                  BppTestCaseMetrics.builder()
                      .numberBins(response.getBins().size())
                      .averageTardinessStoredItems(response.getAverageTardinessStoredItems())
                      .averageLatenessStoredItems(response.getAverageLatenessStoredItems())
                      .maximumLateness(response.getMaximumLateness())
                      .availableCapacity(response.getAvailableCapacity(solution.getBinsCapacity()))
                      .numberIterations(
                          command.getSolvableInstance().getAlgorithm().getLocalSearchAlgorithm()
                                  != null
                              ? response.getNumberIterations()
                              : null)
                      .numberEvaluatedNeighbors(response.getNumberEvaluatedNeighbors())
                      .numberNeighborsGenerated(
                          command.getSolvableInstance().getAlgorithm().getLocalSearchAlgorithm()
                                  != null
                              ? response.getNumberNeighborsGenerated()
                              : null)
                      .fitness(response.getFitness())
                      .evaluationFunctionResultsRecords(
                          response.getEvaluationFunctionResultsRecords())
                      .seconds(stopWatch.getTotalTimeSeconds())
                      .build());
            });

    return CreateBppMetricsByBppInstancesResponse.builder()
        .algorithmsMetrics(
            BppAlgorithmMetrics.builder()
                .averageNumberBins(this.getAverageNumberBins(testCaseResults))
                .averageTardiness(this.getAverageTardiness(testCaseResults))
                .averageLateness(this.getAverageLateness(testCaseResults))
                .averageMaximumLateness(this.getAverageMaximumLateness(testCaseResults))
                .averageAvailableCapacity(this.getAverageAvailableCapacity(testCaseResults))
                .averageNumberIterations(this.getAverageNumberIterations(testCaseResults))
                .averageNumberNeighborsGenerated(
                    this.getAverageNumberNeighborsGenerated(testCaseResults))
                .averageNumberEvaluatedNeighbors(
                    this.getAverageNumberEvaluatedNeighbors(testCaseResults))
                .standardDeviationFitness(this.getStandardDeviationFitness(testCaseResults))
                .averageFitness(this.getAverageFitness(testCaseResults))
                .bestCaseByFitness(this.getBestCaseByFitness(testCaseResults))
                .averageSeconds(this.getAverageSeconds(testCaseResults))
                .algorithm(command.getSolvableInstance().getAlgorithm())
                .testCasesMetrics(new ArrayList<>(testCaseResults))
                .build())
        .build();
  }

  private int getNumberRepetitions(Integer numberRepetitions) {
    return numberRepetitions == null || numberRepetitions == 0
        ? NUMBER_REPETITIONS
        : numberRepetitions;
  }

  private double getAverageSeconds(List<BppTestCaseMetrics> testCaseResults) {
    return testCaseResults.stream().mapToDouble(BppTestCaseMetrics::getSeconds).average().orElse(0);
  }

  private double getAverageTardiness(List<BppTestCaseMetrics> testCaseResults) {
    return testCaseResults.stream()
        .mapToDouble(BppTestCaseMetrics::getAverageTardinessStoredItems)
        .average()
        .orElse(0);
  }

  private double getAverageNumberBins(List<BppTestCaseMetrics> testCaseResults) {
    return testCaseResults.stream().mapToInt(BppTestCaseMetrics::getNumberBins).average().orElse(0);
  }

  private double getAverageLateness(List<BppTestCaseMetrics> testCaseResults) {
    return testCaseResults.stream()
        .mapToDouble(BppTestCaseMetrics::getAverageLatenessStoredItems)
        .average()
        .orElse(0);
  }

  private double getAverageAvailableCapacity(List<BppTestCaseMetrics> testCaseResults) {
    return testCaseResults.stream()
        .mapToDouble(BppTestCaseMetrics::getAvailableCapacity)
        .average()
        .orElse(0);
  }

  private double getAverageMaximumLateness(List<BppTestCaseMetrics> testCaseResults) {
    return testCaseResults.stream()
        .mapToDouble(BppTestCaseMetrics::getMaximumLateness)
        .average()
        .orElse(0);
  }

  private Double getAverageNumberNeighborsGenerated(List<BppTestCaseMetrics> testCaseResults) {
    OptionalDouble result =
        testCaseResults.stream()
            .mapToDouble(
                testCaseMetrics ->
                    Optional.ofNullable(testCaseMetrics.getNumberNeighborsGenerated()).orElse(0))
            .average();
    return result.isPresent() ? result.getAsDouble() : null;
  }

  private Double getAverageNumberEvaluatedNeighbors(List<BppTestCaseMetrics> testCaseResults) {
    OptionalDouble result =
        testCaseResults.stream()
            .mapToDouble(
                testCaseMetrics ->
                    Optional.ofNullable(testCaseMetrics.getNumberEvaluatedNeighbors()).orElse(0))
            .average();
    return result.isPresent() ? result.getAsDouble() : null;
  }

  private Double getAverageNumberIterations(List<BppTestCaseMetrics> testCaseResults) {
    OptionalDouble result =
        testCaseResults.stream()
            .mapToDouble(
                testCaseMetrics ->
                    Optional.ofNullable(testCaseMetrics.getNumberIterations()).orElse(0))
            .average();
    return result.isPresent() ? result.getAsDouble() : null;
  }

  private double getStandardDeviationFitness(List<BppTestCaseMetrics> testCaseResults) {
    double mean =
        testCaseResults.stream()
            .mapToDouble(BppTestCaseMetrics::getFitness)
            .average()
            .orElse(Double.NaN);
    double sumOfSquaredDiffs =
        testCaseResults.stream()
            .mapToDouble(BppTestCaseMetrics::getFitness)
            .map(x -> (x - mean) * (x - mean))
            .sum();
    return Math.sqrt(sumOfSquaredDiffs / testCaseResults.size());
  }

  private double getAverageFitness(List<BppTestCaseMetrics> testCaseResults) {
    return testCaseResults.stream().mapToDouble(BppTestCaseMetrics::getFitness).average().orElse(0);
  }

  private BppTestCaseMetrics getBestCaseByFitness(List<BppTestCaseMetrics> testCaseResults) {
    return testCaseResults.stream()
        .min(Comparator.comparingDouble(BppTestCaseMetrics::getFitness))
        .orElse(null);
  }
}
