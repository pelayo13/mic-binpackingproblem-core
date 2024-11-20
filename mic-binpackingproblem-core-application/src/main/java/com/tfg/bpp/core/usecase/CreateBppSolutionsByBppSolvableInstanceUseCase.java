package com.tfg.bpp.core.usecase;

import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppSolution;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppSolutionsByBppSolvableInstanceUseCasePort;
import com.tfg.bpp.core.service.algorithm.AlgorithmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateBppSolutionsByBppSolvableInstanceUseCase
    implements CreateBppSolutionsByBppSolvableInstanceUseCasePort {

  private static final String CLASS_NAME =
      CreateBppSolutionsByBppSolvableInstanceUseCase.class.getName();

  private final AlgorithmService algorithmService;

  @Override
  public CreateBppSolutionsByBppSolvableInstanceResponse execute(
      CreateBppSolutionsByBppSolvableInstanceCommand command) {
    BppInstance solution =
        this.algorithmService.getSolution(
            command.getSolvableInstance().getInstance(),
            command.getSolvableInstance().getAlgorithm().getGreedyAlgorithmType());

    solution =
        this.algorithmService.getLocalSearchSolution(
            solution, command.getSolvableInstance().getAlgorithm().getLocalSearchAlgorithm());

    return CreateBppSolutionsByBppSolvableInstanceResponse.builder()
        .solution(
            BppSolution.builder()
                .bins(solution.getBins())
                .details(solution.getDetails())
                .build())
        .build();
  }
}
