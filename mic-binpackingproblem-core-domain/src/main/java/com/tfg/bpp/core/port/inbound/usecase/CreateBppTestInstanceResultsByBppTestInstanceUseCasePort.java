package com.tfg.bpp.core.port.inbound.usecase;

import com.tfg.bpp.core.model.BppTestInstance;
import com.tfg.bpp.core.model.BppTestInstanceResults;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public interface CreateBppTestInstanceResultsByBppTestInstanceUseCasePort {

  CreateBppTestInstanceResultsByBppTestInstanceResponse execute(
      @Valid @NotNull
          CreateBppTestInstanceResultsByBppTestInstanceCommand
              createBppSolutionsByBppSolvableInstancesCommand);

  @Builder
  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  final class CreateBppTestInstanceResultsByBppTestInstanceResponse {

    @NotNull private final BppTestInstanceResults testInstanceResults;
  }

  @Builder
  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  final class CreateBppTestInstanceResultsByBppTestInstanceCommand {

    @NotNull private final BppTestInstance testInstance;
  }
}
