package com.tfg.bpp.core.controller;

import com.tfg.bpp.core.mapper.BppSolutionGrpcMapper;
import com.tfg.bpp.core.mapper.BppTestInstanceResultsGrpcMapper;
import com.tfg.bpp.core.model.BppTestResults;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppTestInstanceResultsByBppTestInstanceUseCasePort;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppTestInstanceResultsByBppTestInstanceUseCasePort.CreateBppTestInstanceResultsByBppTestInstanceResponse;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppTestResultsByBppInstanceUseCasePort;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import v1.service.BppTestInstanceResultsServiceGrpc.BppTestInstanceResultsServiceImplBase;
import v1.service.CreateByBppInstanceProto;
import v1.service.CreateByBppTestInstanceProto.CreateByBppTestInstanceRequest;
import v1.service.CreateByBppTestInstanceProto.CreateByBppTestInstanceResponse;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class BppTestInstanceResultsController extends BppTestInstanceResultsServiceImplBase {

  private static final String CLASS_NAME = BppTestInstanceResultsController.class.getName();

  private final CreateBppTestInstanceResultsByBppTestInstanceUseCasePort
      createBppTestInstanceResultsByBppTestInstanceUseCasePort;

  private final CreateBppTestResultsByBppInstanceUseCasePort
      createBppTestResultsByBppInstanceUseCasePort;

  private final BppTestInstanceResultsGrpcMapper bppTestInstanceResultsGrpcMapper;

  private final BppSolutionGrpcMapper bppSolutionGrpcMapper;

  @Override
  public void createByBppTestInstance(
      CreateByBppTestInstanceRequest request,
      StreamObserver<CreateByBppTestInstanceResponse> responseObserver) {
    log.info("[start] {}.createByBppTestInstance", CLASS_NAME);

    CreateBppTestInstanceResultsByBppTestInstanceResponse response =
        this.createBppTestInstanceResultsByBppTestInstanceUseCasePort.execute(
            this.bppTestInstanceResultsGrpcMapper
                .toCreateBppTestInstanceResultsByBppTestInstanceCommand(request));

    log.info("[end] {}.createByBppTestInstance", CLASS_NAME);

    responseObserver.onNext(
        this.bppTestInstanceResultsGrpcMapper.toCreateByBppTestInstanceResponse(response));
    responseObserver.onCompleted();
  }

  @Override
  public void createByBppInstance(
      CreateByBppInstanceProto.CreateByBppInstanceRequest request,
      StreamObserver<CreateByBppInstanceProto.CreateByBppInstanceResponse> responseObserver) {
    log.info("[start] {}.createByBppInstance", CLASS_NAME);

    List<BppTestResults> testResults = new ArrayList<>();
    request
        .getInstancesList()
        .forEach(
            instance -> {
              testResults.add(
                  this.createBppTestResultsByBppInstanceUseCasePort
                      .execute(
                          this.bppSolutionGrpcMapper.toCreateBppTestResultsByBppInstanceCommand(
                              instance))
                      .getTestResults());
            });

    log.info("[end] {}.createByBppInstance", CLASS_NAME);

    responseObserver.onNext(
        CreateByBppInstanceProto.CreateByBppInstanceResponse.newBuilder()
            .addAllTestResults(
                this.bppTestInstanceResultsGrpcMapper.toBppTestResultsListProto(testResults))
            .build());
    responseObserver.onCompleted();
  }
}
