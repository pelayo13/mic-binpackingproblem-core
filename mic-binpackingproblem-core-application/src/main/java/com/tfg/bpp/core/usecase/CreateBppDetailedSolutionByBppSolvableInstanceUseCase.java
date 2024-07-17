package com.tfg.bpp.core.usecase;

import com.tfg.bpp.core.model.BppDetailedSolution;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppLocalSearchType;
import com.tfg.bpp.core.model.BppSolvableInstance;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppDetailedSolutionByBppSolvableInstanceUseCasePort;
import com.tfg.bpp.core.service.algorithm.AlgorithmServiceFactory;
import com.tfg.bpp.core.service.localsearch.LocalSearchServiceFactory;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateBppDetailedSolutionByBppSolvableInstanceUseCase
    implements CreateBppDetailedSolutionByBppSolvableInstanceUseCasePort {

  private static final String CLASS_NAME =
      CreateBppDetailedSolutionByBppSolvableInstanceUseCase.class.getName();

  private final ApplicationContext applicationContext;

  @Override
  public CreateBppDetailedSolutionByBppSolvableInstanceResponse execute(
      CreateBppDetailedSolutionByBppSolvableInstanceCommand
          createBppSolutionByBppSolvableInstanceCommand) {
    BppDetailedSolution solutionGreedyAlgorithm =
        this.getSolution(createBppSolutionByBppSolvableInstanceCommand.getSolvableInstance());
    BppDetailedSolution solutionLocalSearch =
        BppDetailedSolution.builder().recordInstances(new ArrayList<>()).build();

    if (ObjectUtils.isNotEmpty(solutionGreedyAlgorithm.getRecordInstances())) {
      solutionLocalSearch.setRecordInstances(
          this.getLocalSearchSolution(
                  solutionGreedyAlgorithm
                      .getRecordInstances()
                      .get(solutionGreedyAlgorithm.getRecordInstances().size() - 1),
                  createBppSolutionByBppSolvableInstanceCommand
                      .getSolvableInstance()
                      .getLocalSearchType())
              .getRecordInstances());
    } else {
      this.getLocalSearchSolution(
          createBppSolutionByBppSolvableInstanceCommand.getSolvableInstance().getInstance(),
          createBppSolutionByBppSolvableInstanceCommand.getSolvableInstance().getLocalSearchType());
    }

    return CreateBppDetailedSolutionByBppSolvableInstanceResponse.builder()
        .detailedSolution(
            BppDetailedSolution.builder()
                .recordInstances(
                    Stream.concat(
                            solutionGreedyAlgorithm.getRecordInstances().stream(),
                            solutionLocalSearch.getRecordInstances().stream())
                        .toList())
                .build())
        .build();
  }

  private BppDetailedSolution getSolution(BppSolvableInstance solvableInstance) {
    return Optional.ofNullable(solvableInstance.getGreedyAlgorithmType())
        .map(
            bppGreedyAlgorithmType ->
                this.applicationContext.getBean(
                    AlgorithmServiceFactory.valueOf(bppGreedyAlgorithmType.name())
                        .getAlgorithmServiceClass()))
        .map(
            algorithmService ->
                algorithmService.getDetailedSolution(solvableInstance.getInstance()))
        .orElse(BppDetailedSolution.builder().recordInstances(new ArrayList<>()).build());
  }

  private BppDetailedSolution getLocalSearchSolution(
      BppInstance solution, BppLocalSearchType bppLocalSearchType) {
    return Optional.ofNullable(bppLocalSearchType)
        .map(
            localSearchType ->
                this.applicationContext.getBean(
                    LocalSearchServiceFactory.valueOf(localSearchType.name())
                        .getLocalSearchServiceClass()))
        .map(localSearchService -> localSearchService.getDetailedSolution(solution))
        .orElse(BppDetailedSolution.builder().recordInstances(new ArrayList<>()).build());
  }
}
