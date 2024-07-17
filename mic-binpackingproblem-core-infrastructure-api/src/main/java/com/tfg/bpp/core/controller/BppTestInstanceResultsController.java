package com.tfg.bpp.core.controller;

import com.tfg.bpp.core.mapper.BppTestInstanceResultsGrpcMapper;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppTestInstanceResultsByBppTestInstanceUseCasePort;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppTestInstanceResultsByBppTestInstanceUseCasePort.CreateBppTestInstanceResultsByBppTestInstanceResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import v1.service.BppTestInstanceResultsServiceGrpc.BppTestInstanceResultsServiceImplBase;
import v1.service.CreateByBppTestInstanceProto.CreateByBppTestInstanceRequest;
import v1.service.CreateByBppTestInstanceProto.CreateByBppTestInstanceResponse;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class BppTestInstanceResultsController extends BppTestInstanceResultsServiceImplBase {

  private static final String CLASS_NAME = BppTestInstanceResultsController.class.getName();

  private final CreateBppTestInstanceResultsByBppTestInstanceUseCasePort
      createBppTestInstanceResultsByBppTestInstanceUseCasePort;

  private final BppTestInstanceResultsGrpcMapper bppTestInstanceResultsGrpcMapper;

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
}
