package com.tfg.bpp.core.port.inbound.usecase;

import com.tfg.bpp.core.model.BppSolution;
import com.tfg.bpp.core.model.BppSolvableInstance;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface CreateBppSolutionByBppSolvableInstanceUseCasePort {

  CreateBppSolutionByBppSolvableInstanceResponse execute(
      @Valid @NotNull
          CreateBppSolutionByBppSolvableInstanceCommand
              createBppSolutionByBppSolvableInstanceCommand);

  @Builder
  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  final class CreateBppSolutionByBppSolvableInstanceResponse {

    @NotNull private final BppSolution solution;
  }

  @Builder
  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  final class CreateBppSolutionByBppSolvableInstanceCommand {

    @NotNull private final BppSolvableInstance solvableInstance;
  }
}
