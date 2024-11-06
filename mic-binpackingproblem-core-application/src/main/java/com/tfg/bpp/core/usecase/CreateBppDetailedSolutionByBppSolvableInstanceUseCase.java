package com.tfg.bpp.core.usecase;

import com.tfg.bpp.core.model.BppDetailedSolution;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppDetailedSolutionByBppSolvableInstanceUseCasePort;
import com.tfg.bpp.core.service.algorithm.AlgorithmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateBppDetailedSolutionByBppSolvableInstanceUseCase
    implements CreateBppDetailedSolutionByBppSolvableInstanceUseCasePort {

  private static final String CLASS_NAME =
      CreateBppDetailedSolutionByBppSolvableInstanceUseCase.class.getName();

  private final AlgorithmService algorithmService;

  @Override
  public CreateBppDetailedSolutionByBppSolvableInstanceResponse execute(
      CreateBppDetailedSolutionByBppSolvableInstanceCommand command) {
    BppInstance solution =
        this.algorithmService.getSolution(
            command.getSolvableInstance().getInstance(),
            command.getSolvableInstance().getAlgorithm().getGreedyAlgorithmType());

    solution =
        this.algorithmService.getLocalSearchSolution(
            solution, command.getSolvableInstance().getAlgorithm().getLocalSearchAlgorithm());

    return CreateBppDetailedSolutionByBppSolvableInstanceResponse.builder()
        .detailedSolution(
            BppDetailedSolution.builder().recordInstances(solution.getInstancesRecord()).build())
        .build();
  }
}
