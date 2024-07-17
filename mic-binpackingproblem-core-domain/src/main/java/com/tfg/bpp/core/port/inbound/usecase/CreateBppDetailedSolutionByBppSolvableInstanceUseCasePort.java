package com.tfg.bpp.core.port.inbound.usecase;

import com.tfg.bpp.core.model.BppDetailedSolution;
import com.tfg.bpp.core.model.BppSolvableInstance;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface CreateBppDetailedSolutionByBppSolvableInstanceUseCasePort {

  CreateBppDetailedSolutionByBppSolvableInstanceResponse execute(
      @Valid @NotNull
          CreateBppDetailedSolutionByBppSolvableInstanceCommand
              createBppDetailedSolutionByBppSolvableInstanceCommand);

  @Builder
  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  final class CreateBppDetailedSolutionByBppSolvableInstanceResponse {

    @NotNull private final BppDetailedSolution detailedSolution;
  }

  @Builder
  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  final class CreateBppDetailedSolutionByBppSolvableInstanceCommand {

    @NotNull private final BppSolvableInstance solvableInstance;
  }
}
