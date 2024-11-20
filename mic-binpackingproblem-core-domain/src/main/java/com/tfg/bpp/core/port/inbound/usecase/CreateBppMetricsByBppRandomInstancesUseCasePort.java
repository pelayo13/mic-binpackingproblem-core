package com.tfg.bpp.core.port.inbound.usecase;

import com.tfg.bpp.core.model.BppAlgorithm;
import com.tfg.bpp.core.model.BppInstanceMetrics;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public interface CreateBppMetricsByBppRandomInstancesUseCasePort {

  CreateBppMetricsByBppRandomInstancesResponse execute(
      @Valid @NotNull CreateBppMetricsByBppRandomInstancesCommand command);

  @Builder
  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  final class CreateBppMetricsByBppRandomInstancesResponse {

    @NotNull private final List<BppInstanceMetrics> instancesMetrics;
  }

  @Builder
  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  final class CreateBppMetricsByBppRandomInstancesCommand {

    @NonNull private Integer binsCapacity;

    @NonNull private Integer minItemsSize;

    @NonNull private Integer maxItemsSize;

    @NonNull private Integer minItemsDueDate;

    @NonNull private Integer maxItemsDueDate;

    @NonNull @Valid private List<@Min(0) Integer> numberItems;

    @NonNull private Integer numberRepetitions;

    @NonNull @Valid private List<@Valid BppAlgorithm> algorithms;
  }
}
