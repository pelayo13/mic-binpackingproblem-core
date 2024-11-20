package com.tfg.bpp.core.port.inbound.usecase;

import com.tfg.bpp.core.model.BppAlgorithmMetrics;
import com.tfg.bpp.core.model.BppSolvableInstance;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface CreateBppMetricsByBppInstancesUseCasePort {

  CreateBppMetricsByBppInstancesResponse execute(
      @Valid @NotNull
      CreateBppMetricsByBppInstancesUseCasePort.CreateBppMetricsByBppInstancesCommand createBppMetricsByBppInstancesCommand);

  @Builder
  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  final class CreateBppMetricsByBppInstancesResponse {

    @NotNull private final BppAlgorithmMetrics algorithmsMetrics;
  }

  @Builder
  @Data
  @RequiredArgsConstructor
  final class CreateBppMetricsByBppInstancesCommand {

    @NotNull private final BppSolvableInstance solvableInstance;

    private final Integer numberRepetitions;
  }
}
