package com.tfg.bpp.core.usecase;

import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppLocalSearchType;
import com.tfg.bpp.core.model.BppSolution;
import com.tfg.bpp.core.model.BppSolvableInstance;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppSolutionByBppSolvableInstanceUseCasePort;
import com.tfg.bpp.core.service.algorithm.AlgorithmServiceFactory;
import com.tfg.bpp.core.service.localsearch.LocalSearchServiceFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateBppSolutionByBppSolvableInstanceUseCase
    implements CreateBppSolutionByBppSolvableInstanceUseCasePort {

  private static final String CLASS_NAME =
      CreateBppSolutionByBppSolvableInstanceUseCase.class.getName();

  private final ApplicationContext applicationContext;

  @Override
  public CreateBppSolutionByBppSolvableInstanceResponse execute(
      CreateBppSolutionByBppSolvableInstanceCommand createBppSolutionByBppSolvableInstanceCommand) {
    BppInstance solution =
        this.getSolution(createBppSolutionByBppSolvableInstanceCommand.getSolvableInstance());

    solution =
        this.getLocalSearchSolution(
            solution,
            createBppSolutionByBppSolvableInstanceCommand
                .getSolvableInstance()
                .getLocalSearchType());

    return CreateBppSolutionByBppSolvableInstanceResponse.builder()
        .solution(BppSolution.builder().bins(solution.getBins()).build())
        .build();
  }

  private BppInstance getSolution(BppSolvableInstance solvableInstance) {
    return Optional.ofNullable(solvableInstance.getGreedyAlgorithmType())
        .map(
            bppGreedyAlgorithmType ->
                this.applicationContext.getBean(
                    AlgorithmServiceFactory.valueOf(bppGreedyAlgorithmType.name())
                        .getAlgorithmServiceClass()))
        .map(algorithmService -> algorithmService.getSolution(solvableInstance.getInstance()))
        .orElse(solvableInstance.getInstance());
  }

  private BppInstance getLocalSearchSolution(
      BppInstance solution, BppLocalSearchType bppLocalSearchType) {
    return Optional.ofNullable(bppLocalSearchType)
        .map(
            localSearchType ->
                this.applicationContext.getBean(
                    LocalSearchServiceFactory.valueOf(localSearchType.name())
                        .getLocalSearchServiceClass()))
        .map(localSearchService -> localSearchService.getSolution(solution))
        .orElse(solution);
  }
}
