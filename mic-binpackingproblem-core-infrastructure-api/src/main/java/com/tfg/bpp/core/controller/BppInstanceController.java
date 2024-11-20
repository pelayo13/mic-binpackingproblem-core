package com.tfg.bpp.core.controller;

import com.tfg.bpp.core.mapper.BppInstanceControllerGrpcMapper;
import com.tfg.bpp.core.model.BppSolution;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppSolutionsByBppSolvableInstanceUseCasePort;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import v1.service.BppInstanceServiceGrpc;
import v1.service.CreateBppSolutionsBySolvableInstancesProto.CreateBppSolutionsByBppSolvableInstancesRequest;
import v1.service.CreateBppSolutionsBySolvableInstancesProto.CreateBppSolutionsByBppSolvableInstancesResponse;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class BppInstanceController extends BppInstanceServiceGrpc.BppInstanceServiceImplBase {

  private static final String CLASS_NAME = BppInstanceController.class.getName();

  private final CreateBppSolutionsByBppSolvableInstanceUseCasePort
          createBppSolutionsByBppSolvableInstanceUseCasePort;

  private final BppInstanceControllerGrpcMapper bppInstanceControllerGrpcMapper;

  @Override
  public void createBppSolutionsByBppSolvableInstances(
      CreateBppSolutionsByBppSolvableInstancesRequest request,
      StreamObserver<CreateBppSolutionsByBppSolvableInstancesResponse> responseObserver) {
    log.info("[start] {}.createBppSolutionsByBppSolvableInstances", CLASS_NAME);

    List<BppSolution> solutions = new ArrayList<>();
    request
        .getSolvableInstancesList()
        .forEach(
            instance -> {
              solutions.add(
                  this.createBppSolutionsByBppSolvableInstanceUseCasePort
                      .execute(
                          this.bppInstanceControllerGrpcMapper
                              .toCreateBppSolutionByBppSolvableInstanceCommand(instance))
                      .getSolution());
            });

    log.info("[end] {}.createBppSolutionsByBppSolvableInstances", CLASS_NAME);

    responseObserver.onNext(
        CreateBppSolutionsByBppSolvableInstancesResponse.newBuilder()
            .addAllSolutions(this.bppInstanceControllerGrpcMapper.toBppSolutionsProto(solutions))
            .build());
    responseObserver.onCompleted();
  }
}
