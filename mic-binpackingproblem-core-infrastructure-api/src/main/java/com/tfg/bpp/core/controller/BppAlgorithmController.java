package com.tfg.bpp.core.controller;

import com.tfg.bpp.core.mapper.BppAlgorithmControllerGrpcMapper;
import com.tfg.bpp.core.model.BppAlgorithmMetrics;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppMetricsByBppInstancesUseCasePort;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppMetricsByBppRandomInstancesUseCasePort;
import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import v1.service.BppAlgorithmServiceGrpc;
import v1.service.CreateBppMetricsByBppInstancesProto.CreateBppMetricsByBppInstancesRequest;
import v1.service.CreateBppMetricsByBppInstancesProto.CreateBppMetricsByBppInstancesResponse;
import v1.service.CreateBppMetricsByBppRandomInstancesProto.CreateBppMetricsByBppRandomInstancesRequest;
import v1.service.CreateBppMetricsByBppRandomInstancesProto.CreateBppMetricsByBppRandomInstancesResponse;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class BppAlgorithmController extends BppAlgorithmServiceGrpc.BppAlgorithmServiceImplBase {

  private static final String CLASS_NAME = BppAlgorithmController.class.getName();

  private final CreateBppMetricsByBppRandomInstancesUseCasePort
      createBppMetricsByBppRandomInstancesUseCasePort;

  private final CreateBppMetricsByBppInstancesUseCasePort createBppMetricsByBppInstancesUseCasePort;

  private final BppAlgorithmControllerGrpcMapper bppAlgorithmControllerGrpcMapper;

  @Override
  public void createBppMetricsByBppRandomInstances(
      CreateBppMetricsByBppRandomInstancesRequest request,
      StreamObserver<CreateBppMetricsByBppRandomInstancesResponse> responseObserver) {
    log.info("[start] {}.createByBppTestInstance", CLASS_NAME);

    CreateBppMetricsByBppRandomInstancesUseCasePort.CreateBppMetricsByBppRandomInstancesResponse
        response =
            this.createBppMetricsByBppRandomInstancesUseCasePort.execute(
                this.bppAlgorithmControllerGrpcMapper.toCreateBppMetricsByBppRandomInstancesCommand(
                    request));

    log.info("[end] {}.createByBppTestInstance", CLASS_NAME);

    responseObserver.onNext(
        CreateBppMetricsByBppRandomInstancesResponse.newBuilder()
            .addAllInstancesMetrics(
                this.bppAlgorithmControllerGrpcMapper.toBppInstanceMetricsListProto(
                    response.getInstancesMetrics()))
            .build());
    responseObserver.onCompleted();
  }

  @Override
  public void createBppMetricsByBppInstances(
      CreateBppMetricsByBppInstancesRequest request,
      StreamObserver<CreateBppMetricsByBppInstancesResponse> responseObserver) {
    log.info("[start] {}.createByBppInstance", CLASS_NAME);

    List<BppAlgorithmMetrics> response =
        request.getInstancesList().stream()
            .flatMap(
                instance ->
                    request.getAlgorithmsList().stream()
                        .map(
                            algorithm ->
                                this.createBppMetricsByBppInstancesUseCasePort
                                    .execute(
                                        this.bppAlgorithmControllerGrpcMapper
                                            .toCreateBppTestResultsByBppInstanceCommand(
                                                instance, algorithm, request.getNumberRepetitions()))
                                    .getAlgorithmsMetrics()))
            .collect(Collectors.toList());

    log.info("[end] {}.createByBppInstance", CLASS_NAME);

    responseObserver.onNext(
        CreateBppMetricsByBppInstancesResponse.newBuilder()
            .addAllAlgorithmsMetrics(
                this.bppAlgorithmControllerGrpcMapper.toBppAlgorithmMetricsListProto(response))
            .build());
    responseObserver.onCompleted();
  }
}
