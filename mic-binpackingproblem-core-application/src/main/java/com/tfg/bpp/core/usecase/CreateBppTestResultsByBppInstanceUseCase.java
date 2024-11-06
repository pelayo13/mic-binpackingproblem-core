package com.tfg.bpp.core.usecase;

import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppLocalSearchSolution;
import com.tfg.bpp.core.model.BppTestCaseResults;
import com.tfg.bpp.core.model.BppTestResults;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppTestResultsByBppInstanceUseCasePort;
import com.tfg.bpp.core.service.algorithm.AlgorithmService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateBppTestResultsByBppInstanceUseCase
    implements CreateBppTestResultsByBppInstanceUseCasePort {

  public static final int NUMBER_REPETITIONS = 10;
  private static final String CLASS_NAME = CreateBppTestResultsByBppInstanceUseCase.class.getName();

  private final AlgorithmService algorithmService;

  @Override
  public CreateBppTestResultsByBppInstanceResponse execute(
      CreateBppTestResultsByBppInstanceCommand command) {
    List<BppTestCaseResults> testCaseResults = new ArrayList<>();
    IntStream.range(0, NUMBER_REPETITIONS)
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
                  BppTestCaseResults.builder()
                      .numberBins(response.getBins().size())
                      .averageTardinessStoredItems(response.getAverageTardinessStoredItems())
                      .averageLatenessStoredItems(response.getAverageLatenessStoredItems())
                      .maximumLateness(response.getMaximumLateness())
                      .availableCapacity(response.getAvailableCapacity(solution.getBinsCapacity()))
                      .numberIterations(response.getNumberIterations())
                      .numberEvaluatedNeighbors(response.getNumberEvaluatedNeighbors())
                      .numberNeighborsGenerated(response.getNumberNeighborsGenerated())
                      .fitness(response.getFitness())
                      .evaluationFunctionResultsRecords(
                          new ArrayList<>(response.getEvaluationFunctionResultsRecords()))
                      .seconds(stopWatch.getTotalTimeSeconds())
                      .build());
            });

    return CreateBppTestResultsByBppInstanceResponse.builder()
        .testResults(
            BppTestResults.builder()
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
                .testCaseResults(new ArrayList<>(testCaseResults))
                .build())
        .build();
  }

  private double getAverageSeconds(List<BppTestCaseResults> testCaseResults) {
    return testCaseResults.stream().mapToDouble(BppTestCaseResults::getSeconds).average().orElse(0);
  }

  private double getAverageTardiness(List<BppTestCaseResults> testCaseResults) {
    return testCaseResults.stream()
        .mapToDouble(BppTestCaseResults::getAverageTardinessStoredItems)
        .average()
        .orElse(0);
  }

  private double getAverageNumberBins(List<BppTestCaseResults> testCaseResults) {
    return testCaseResults.stream().mapToInt(BppTestCaseResults::getNumberBins).average().orElse(0);
  }

  private double getAverageLateness(List<BppTestCaseResults> testCaseResults) {
    return testCaseResults.stream()
        .mapToDouble(BppTestCaseResults::getAverageLatenessStoredItems)
        .average()
        .orElse(0);
  }

  private double getAverageAvailableCapacity(List<BppTestCaseResults> testCaseResults) {
    return testCaseResults.stream()
        .mapToDouble(BppTestCaseResults::getAvailableCapacity)
        .average()
        .orElse(0);
  }

  private double getAverageMaximumLateness(List<BppTestCaseResults> testCaseResults) {
    return testCaseResults.stream()
        .mapToDouble(BppTestCaseResults::getMaximumLateness)
        .average()
        .orElse(0);
  }

  private double getAverageNumberNeighborsGenerated(List<BppTestCaseResults> testCaseResults) {
    return testCaseResults.stream()
        .mapToDouble(BppTestCaseResults::getNumberNeighborsGenerated)
        .average()
        .orElse(0);
  }

  private Double getAverageNumberEvaluatedNeighbors(List<BppTestCaseResults> testCaseResults) {
    OptionalDouble result =
        testCaseResults.stream()
            .mapToDouble(BppTestCaseResults::getNumberEvaluatedNeighbors)
            .average();
    return result.isPresent() ? result.getAsDouble() : null;
  }

  private double getAverageNumberIterations(List<BppTestCaseResults> testCaseResults) {
    return testCaseResults.stream()
        .mapToDouble(BppTestCaseResults::getNumberEvaluatedNeighbors)
        .average()
        .orElse(0);
  }

  private double getStandardDeviationFitness(List<BppTestCaseResults> testCaseResults) {
    double mean =
        testCaseResults.stream()
            .mapToDouble(BppTestCaseResults::getFitness)
            .average()
            .orElse(Double.NaN);
    double sumOfSquaredDiffs =
        testCaseResults.stream()
            .mapToDouble(BppTestCaseResults::getFitness)
            .map(x -> (x - mean) * (x - mean))
            .sum();
    return Math.sqrt(sumOfSquaredDiffs / testCaseResults.size());
  }

  private double getAverageFitness(List<BppTestCaseResults> testCaseResults) {
    return testCaseResults.stream().mapToDouble(BppTestCaseResults::getFitness).average().orElse(0);
  }

  private BppTestCaseResults getBestCaseByFitness(List<BppTestCaseResults> testCaseResults) {
    return testCaseResults.stream()
        .min(Comparator.comparingDouble(BppTestCaseResults::getFitness))
        .orElse(null);
  }
}
