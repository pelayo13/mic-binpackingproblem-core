package com.tfg.bpp.core.mapper;

import com.google.protobuf.Int32Value;
import com.tfg.bpp.core.config.CentralMapperConfig;
import com.tfg.bpp.core.model.BppSolvableInstance;
import com.tfg.bpp.core.model.BppTestCaseMetrics;
import com.tfg.bpp.core.model.BppInstanceMetrics;
import com.tfg.bpp.core.model.BppAlgorithmMetrics;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppMetricsByBppInstancesUseCasePort;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppMetricsByBppRandomInstancesUseCasePort;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import v1.model.BppAlgorithmMetricsProto;
import v1.model.BppAlgorithmProto;
import v1.model.BppInstanceMetricsProto;
import v1.model.BppInstanceProto;
import v1.model.BppTestCaseMetricsProto;
import v1.service.CreateBppMetricsByBppRandomInstancesProto;

import java.util.List;

@Mapper(
    config = CentralMapperConfig.class,
    uses = {GrpcMapper.class, BppAlgorithmGrpcMapper.class, BppInstanceControllerGrpcMapper.class})
public interface BppAlgorithmControllerGrpcMapper {

  @Mapping(target = "solvableInstance", expression = "java(this.toBppSolvableInstance(instance, algorithm))")
  CreateBppMetricsByBppInstancesUseCasePort.CreateBppMetricsByBppInstancesCommand
  toCreateBppTestResultsByBppInstanceCommand(
          BppInstanceProto.BppInstance instance, BppAlgorithmProto.BppAlgorithm algorithm, Int32Value numberRepetitions);

  @Mapping(target = "numberItems", source = "numberItemsList")
  @Mapping(target = "algorithms", source = "algorithmsList")
  CreateBppMetricsByBppRandomInstancesUseCasePort.CreateBppMetricsByBppRandomInstancesCommand
      toCreateBppMetricsByBppRandomInstancesCommand(
          CreateBppMetricsByBppRandomInstancesProto.CreateBppMetricsByBppRandomInstancesRequest request);

  List<BppAlgorithmMetricsProto.BppAlgorithmMetrics> toBppAlgorithmMetricsListProto(List<BppAlgorithmMetrics> algorithmMetricsList);


  BppSolvableInstance toBppSolvableInstance(
          BppInstanceProto.BppInstance instance, BppAlgorithmProto.BppAlgorithm algorithm);

  List<BppInstanceMetricsProto.BppInstanceMetrics> toBppInstanceMetricsListProto(
          List<BppInstanceMetrics> instanceMetricsList);

  @Mapping(target = "algorithmsMetricsList", source = "algorithmsMetrics")
  BppInstanceMetricsProto.BppInstanceMetrics toBppInstanceMetricsProto(
      BppInstanceMetrics instanceMetrics);

  @Mapping(target = "testCasesMetricsList", source = "testCasesMetrics")
  BppAlgorithmMetricsProto.BppAlgorithmMetrics toBppAlgorithmMetricsProto(BppAlgorithmMetrics bppAlgorithmMetrics);

  @Mapping(target = "evaluationFunctionResultsRecordsList", source = "evaluationFunctionResultsRecords")
  BppTestCaseMetricsProto.BppTestCaseMetrics toBppTestCaseMetricsProto(
      BppTestCaseMetrics bppTestCaseMetrics);
}
