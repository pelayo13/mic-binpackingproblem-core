package com.tfg.bpp.core.usecase;

import com.tfg.bpp.core.model.BppAlgorithm;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppItem;
import com.tfg.bpp.core.model.BppSolvableInstance;
import com.tfg.bpp.core.model.BppTestInstance;
import com.tfg.bpp.core.model.BppTestInstanceResults;
import com.tfg.bpp.core.model.BppTestItemsResults;
import com.tfg.bpp.core.model.BppTestResults;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppTestInstanceResultsByBppTestInstanceUseCasePort;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppTestResultsByBppInstanceUseCasePort.CreateBppTestResultsByBppInstanceCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateBppTestInstanceResultsByBppTestInstanceUseCase
    implements CreateBppTestInstanceResultsByBppTestInstanceUseCasePort {

  private final CreateBppTestResultsByBppInstanceUseCase createBppTestResultsByBppInstanceUseCase;

  @Override
  public CreateBppTestInstanceResultsByBppTestInstanceResponse execute(
      CreateBppTestInstanceResultsByBppTestInstanceCommand
          createBppSolutionsByBppSolvableInstancesCommand) {
    BppTestInstance bppTestInstance =
        createBppSolutionsByBppSolvableInstancesCommand.getTestInstance();

    List<BppTestItemsResults> testItemsResults = new ArrayList<>();
    List<BppTestResults> testResults = new ArrayList<>();

    bppTestInstance
        .getNumberItems()
        .forEach(
            numberItems -> {
              bppTestInstance
                  .getAlgorithms()
                  .forEach(
                      bppAlgorithm -> {
                        BppSolvableInstance instance =
                            this.buildSolvableInstanceByTestInstance(
                                bppTestInstance, numberItems, bppAlgorithm);
                        CreateBppTestResultsByBppInstanceCommand command =
                            CreateBppTestResultsByBppInstanceCommand.builder()
                                .solvableInstance(instance)
                                .build();

                        testResults.add(
                            this.createBppTestResultsByBppInstanceUseCase
                                .execute(command)
                                .getTestResults());
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

  private BppSolvableInstance buildSolvableInstanceByTestInstance(
      BppTestInstance bppTestInstance, Integer numberItems, BppAlgorithm bppAlgorithm) {
    return BppSolvableInstance.builder()
        .instance(
            BppInstance.builder()
                .binsCapacity(bppTestInstance.getBinsCapacity())
                .items(this.buildItems(bppTestInstance, numberItems))
                .bins(new ArrayList<>())
                .build())
        .algorithm(
            BppAlgorithm.builder()
                .greedyAlgorithmType(bppAlgorithm.getGreedyAlgorithmType())
                .localSearchAlgorithm(bppAlgorithm.getLocalSearchAlgorithm())
                .build())
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
