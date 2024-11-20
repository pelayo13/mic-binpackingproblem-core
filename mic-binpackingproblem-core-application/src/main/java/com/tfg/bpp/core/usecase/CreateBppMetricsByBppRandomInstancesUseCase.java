package com.tfg.bpp.core.usecase;

import com.tfg.bpp.core.model.BppAlgorithm;
import com.tfg.bpp.core.model.BppAlgorithmMetrics;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppInstanceMetrics;
import com.tfg.bpp.core.model.BppItem;
import com.tfg.bpp.core.model.BppSolvableInstance;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppMetricsByBppInstancesUseCasePort.CreateBppMetricsByBppInstancesCommand;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppMetricsByBppRandomInstancesUseCasePort;
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
public class CreateBppMetricsByBppRandomInstancesUseCase
    implements CreateBppMetricsByBppRandomInstancesUseCasePort {

  private final CreateBppMetricsByBppInstancesUseCase createBppTestResultsByBppInstanceUseCase;

  @Override
  public CreateBppMetricsByBppRandomInstancesResponse execute(
      CreateBppMetricsByBppRandomInstancesCommand command) {
    List<BppInstanceMetrics> instanceMetricsList = new ArrayList<>();
    List<BppAlgorithmMetrics> algorithmMetricsList = new ArrayList<>();

    command
        .getNumberItems()
        .forEach(
            numberItems -> {
              command
                  .getAlgorithms()
                  .forEach(
                      bppAlgorithm -> {
                        BppSolvableInstance instance =
                            this.buildSolvableInstanceByTestInstance(
                                command, numberItems, bppAlgorithm);
                        CreateBppMetricsByBppInstancesCommand instanceMetricsCommand =
                            CreateBppMetricsByBppInstancesCommand.builder()
                                .solvableInstance(instance)
                                .numberRepetitions(command.getNumberRepetitions())
                                .build();

                        algorithmMetricsList.add(
                            this.createBppTestResultsByBppInstanceUseCase
                                .execute(instanceMetricsCommand)
                                .getAlgorithmsMetrics());
                      });

              instanceMetricsList.add(
                  BppInstanceMetrics.builder()
                      .algorithmsMetrics(new ArrayList<>(algorithmMetricsList))
                      .numberItems(numberItems)
                      .build());
              algorithmMetricsList.clear();
            });

    return CreateBppMetricsByBppRandomInstancesResponse.builder()
        .instancesMetrics(instanceMetricsList)
        .build();
  }

  private BppSolvableInstance buildSolvableInstanceByTestInstance(
      CreateBppMetricsByBppRandomInstancesCommand command,
      Integer numberItems,
      BppAlgorithm bppAlgorithm) {
    return BppSolvableInstance.builder()
        .instance(
            BppInstance.builder()
                .binsCapacity(command.getBinsCapacity())
                .items(this.buildItems(command, numberItems))
                .bins(new ArrayList<>())
                .build())
        .algorithm(
            BppAlgorithm.builder()
                .greedyAlgorithmType(bppAlgorithm.getGreedyAlgorithmType())
                .localSearchAlgorithm(bppAlgorithm.getLocalSearchAlgorithm())
                .build())
        .build();
  }

  private List<BppItem> buildItems(
      CreateBppMetricsByBppRandomInstancesCommand command, Integer numberItems) {
    return Stream.generate(() -> this.buildItem(command))
        .limit(numberItems)
        .collect(Collectors.toList());
  }

  private BppItem buildItem(CreateBppMetricsByBppRandomInstancesCommand command) {
    return BppItem.builder()
        .size(
            this.getRandomValueBetweenInclusive(
                command.getMinItemsSize(), command.getMaxItemsSize()))
        .dueDate(
            this.getRandomValueBetweenInclusive(
                command.getMinItemsDueDate(), command.getMaxItemsDueDate()))
        .build();
  }

  private int getRandomValueBetweenInclusive(int minInclusive, int maxInclusive) {
    Random random = new Random();
    return random.nextInt(maxInclusive - minInclusive + 1) + minInclusive;
  }
}
