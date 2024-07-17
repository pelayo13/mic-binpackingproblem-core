package com.tfg.bpp.core.usecase;

import com.tfg.bpp.core.model.BppAlgorithm;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppItem;
import com.tfg.bpp.core.model.BppSolvableInstance;
import com.tfg.bpp.core.model.BppTestCaseResults;
import com.tfg.bpp.core.model.BppTestInstance;
import com.tfg.bpp.core.model.BppTestInstanceResults;
import com.tfg.bpp.core.model.BppTestItemsResults;
import com.tfg.bpp.core.model.BppTestResults;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppSolutionByBppSolvableInstanceUseCasePort;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppSolutionByBppSolvableInstanceUseCasePort.CreateBppSolutionByBppSolvableInstanceCommand;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppSolutionByBppSolvableInstanceUseCasePort.CreateBppSolutionByBppSolvableInstanceResponse;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppTestInstanceResultsByBppTestInstanceUseCasePort;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateBppTestInstanceResultsByBppTestInstanceUseCase
    implements CreateBppTestInstanceResultsByBppTestInstanceUseCasePort {

  private static final String CLASS_NAME =
      CreateBppTestInstanceResultsByBppTestInstanceUseCase.class.getName();

  private final CreateBppSolutionByBppSolvableInstanceUseCasePort
      createBppSolutionByBppSolvableInstanceUseCasePort;

  @Override
  public CreateBppTestInstanceResultsByBppTestInstanceResponse execute(
      CreateBppTestInstanceResultsByBppTestInstanceCommand
          createBppSolutionsByBppSolvableInstancesCommand) {
    BppTestInstance bppTestInstance =
        createBppSolutionsByBppSolvableInstancesCommand.getTestInstance();

    StopWatch stopWatch = new StopWatch();
    List<BppTestItemsResults> testItemsResults = new ArrayList<>();
    List<BppTestResults> testResults = new ArrayList<>();
    List<BppTestCaseResults> testCaseResults = new ArrayList<>();

    bppTestInstance
        .getNumberItems()
        .forEach(
            numberItems -> {
              bppTestInstance
                  .getAlgorithms()
                  .forEach(
                      bppAlgorithm -> {
                        IntStream.range(0, bppTestInstance.getNumberInstances())
                            .forEach(
                                repetition -> {
                                  BppSolvableInstance instance =
                                      this.buildSolvableInstanceByTestInstance(
                                          bppTestInstance, numberItems, bppAlgorithm);
                                  CreateBppSolutionByBppSolvableInstanceCommand command =
                                      CreateBppSolutionByBppSolvableInstanceCommand.builder()
                                          .solvableInstance(instance)
                                          .build();

                                  log.info(
                                      "[start] {}.execute - numberItems: {}, algorithm: {}, repetition: {}",
                                      CLASS_NAME,
                                      numberItems,
                                      bppAlgorithm,
                                      repetition);
                                  stopWatch.start();
                                  CreateBppSolutionByBppSolvableInstanceResponse response =
                                      this.createBppSolutionByBppSolvableInstanceUseCasePort
                                          .execute(command);
                                  stopWatch.stop();
                                  log.info(
                                      "[end] {}.execute - numberItems: {}, algorithm: {}, repetition: {}",
                                      CLASS_NAME,
                                      numberItems,
                                      bppAlgorithm,
                                      repetition);

                                  testCaseResults.add(
                                      BppTestCaseResults.builder()
                                          .numberBins(response.getSolution().getBins().size())
                                          .averageTardinessStoredItems(
                                              response
                                                  .getSolution()
                                                  .getAverageTardinessStoredItems())
                                          .seconds(stopWatch.getTotalTimeSeconds())
                                          .build());
                                });

                        testResults.add(
                            BppTestResults.builder()
                                .averageNumberBins(this.getAverageNumberBins(testCaseResults))
                                .averageTardiness(this.getAverageTardiness(testCaseResults))
                                .averageSeconds(this.getAverageSeconds(testCaseResults))
                                .algorithm(bppAlgorithm)
                                .testCaseResults(new ArrayList<>(testCaseResults))
                                .build());
                        testCaseResults.clear();
                      });

              testItemsResults.add(
                  BppTestItemsResults.builder()
                      .testResults(new ArrayList<>(testResults))
                      .numberItems(numberItems)
                      .build());
              testResults.clear();
            });

    return CreateBppTestInstanceResultsByBppTestInstanceResponse.builder()
        .testInstanceResults(
            BppTestInstanceResults.builder().testItemsResults(testItemsResults).build())
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

  private BppSolvableInstance buildSolvableInstanceByTestInstance(
      BppTestInstance bppTestInstance, Integer numberItems, BppAlgorithm bppAlgorithm) {
    return BppSolvableInstance.builder()
        .instance(
            BppInstance.builder()
                .binsCapacity(bppTestInstance.getBinsCapacity())
                .items(this.buildItems(bppTestInstance, numberItems))
                .bins(new ArrayList<>())
                .build())
        .greedyAlgorithmType(bppAlgorithm.getGreedyAlgorithmType())
        .localSearchType(bppAlgorithm.getLocalSearchType())
        .build();
  }

  private List<BppItem> buildItems(BppTestInstance bppTestInstance, Integer numberItems) {
    return Stream.generate(() -> this.buildItem(bppTestInstance))
        .limit(numberItems)
        .collect(Collectors.toList());
  }

  private BppItem buildItem(BppTestInstance bppTestInstance) {
    return BppItem.builder()
        .size(
            this.getRandomValueBetweenInclusive(
                bppTestInstance.getMinItemsSize(), bppTestInstance.getMaxItemsSize()))
        .dueDate(
            this.getRandomValueBetweenInclusive(
                bppTestInstance.getMinItemsDueDate(), bppTestInstance.getMaxItemsDueDate()))
        .build();
  }

  private int getRandomValueBetweenInclusive(int minInclusive, int maxInclusive) {
    Random random = new Random();
    return random.nextInt(maxInclusive - minInclusive + 1) + minInclusive;
  }
}
