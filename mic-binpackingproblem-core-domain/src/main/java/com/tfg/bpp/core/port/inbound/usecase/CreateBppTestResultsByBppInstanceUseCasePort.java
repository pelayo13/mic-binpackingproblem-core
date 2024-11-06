package com.tfg.bpp.core.port.inbound.usecase;

import com.tfg.bpp.core.model.BppSolvableInstance;
import com.tfg.bpp.core.model.BppTestResults;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface CreateBppTestResultsByBppInstanceUseCasePort {

  CreateBppTestResultsByBppInstanceResponse execute(
      @Valid @NotNull
          CreateBppTestResultsByBppInstanceCommand createBppTestResultsByBppInstanceCommand);

  @Builder
  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  final class CreateBppTestResultsByBppInstanceResponse {

    @NotNull private final BppTestResults testResults;
  }

  @Builder
  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  final class CreateBppTestResultsByBppInstanceCommand {

    @NotNull private final BppSolvableInstance solvableInstance;
  }
}
