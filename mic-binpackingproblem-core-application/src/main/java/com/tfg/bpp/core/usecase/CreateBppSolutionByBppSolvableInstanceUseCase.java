package com.tfg.bpp.core.usecase;

import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppSolution;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppSolutionByBppSolvableInstanceUseCasePort;
import com.tfg.bpp.core.service.algorithm.AlgorithmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateBppSolutionByBppSolvableInstanceUseCase
    implements CreateBppSolutionByBppSolvableInstanceUseCasePort {

  private static final String CLASS_NAME =
      CreateBppSolutionByBppSolvableInstanceUseCase.class.getName();

  private final AlgorithmService algorithmService;

  @Override
  public CreateBppSolutionByBppSolvableInstanceResponse execute(
      CreateBppSolutionByBppSolvableInstanceCommand command) {
    BppInstance solution =
        this.algorithmService.getSolution(
            command.getSolvableInstance().getInstance(),
            command.getSolvableInstance().getAlgorithm().getGreedyAlgorithmType());

    solution =
        this.algorithmService.getLocalSearchSolution(
            solution, command.getSolvableInstance().getAlgorithm().getLocalSearchAlgorithm());

    return CreateBppSolutionByBppSolvableInstanceResponse.builder()
        .solution(BppSolution.builder().bins(solution.getBins()).build())
        .build();
  }
}
