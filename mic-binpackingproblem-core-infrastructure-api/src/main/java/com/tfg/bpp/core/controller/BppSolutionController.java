package com.tfg.bpp.core.controller;

import com.tfg.bpp.core.mapper.BppSolutionGrpcMapper;
import com.tfg.bpp.core.model.BppDetailedSolution;
import com.tfg.bpp.core.model.BppSolution;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppDetailedSolutionByBppSolvableInstanceUseCasePort;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppSolutionByBppSolvableInstanceUseCasePort;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import v1.service.BppSolutionServiceGrpc;
import v1.service.CreateBppDetailedSolutionBySolvableInstancesProto.CreateBppDetailedSolutionByBppSolvableInstancesResponse;
import v1.service.CreateBppDetailedSolutionBySolvableInstancesProto.CreateBppDetailedSolutionByBppSolvableInstancesRequest;
import v1.service.CreateBySolvableInstancesProto.CreateByBppSolvableInstancesRequest;
import v1.service.CreateBySolvableInstancesProto.CreateByBppSolvableInstancesResponse;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class BppSolutionController extends BppSolutionServiceGrpc.BppSolutionServiceImplBase {

  private static final String CLASS_NAME = BppSolutionController.class.getName();

  private final CreateBppSolutionByBppSolvableInstanceUseCasePort
      createBppSolutionByBppSolvableInstanceUseCasePort;

  private final CreateBppDetailedSolutionByBppSolvableInstanceUseCasePort
          createBppDetailedSolutionByBppSolvableInstanceUseCasePort;

  private final BppSolutionGrpcMapper bppSolutionGrpcMapper;

  @Override
  public void createByBppSolvableInstances(
      CreateByBppSolvableInstancesRequest request,
      StreamObserver<CreateByBppSolvableInstancesResponse> responseObserver) {
    log.info("[start] {}.createByBppSolvableInstances", CLASS_NAME);

    List<BppSolution> solutions = new ArrayList<>();
    request
        .getSolvableInstancesList()
        .forEach(
            instance -> {
              solutions.add(
                  this.createBppSolutionByBppSolvableInstanceUseCasePort
                      .execute(
                          this.bppSolutionGrpcMapper
                              .toCreateBppSolutionByBppSolvableInstanceCommand(instance))
                      .getSolution());
            });

    log.info("[end] {}.createByBppSolvableInstances", CLASS_NAME);

    responseObserver.onNext(
        CreateByBppSolvableInstancesResponse.newBuilder()
            .addAllSolutions(this.bppSolutionGrpcMapper.toBppSolutionsProto(solutions))
            .build());
    responseObserver.onCompleted();
  }

  @Override
  public void createBppDetailedSolutionByBppSolvableInstances(
          CreateBppDetailedSolutionByBppSolvableInstancesRequest request,
          StreamObserver<CreateBppDetailedSolutionByBppSolvableInstancesResponse> responseObserver) {
    log.info("[start] {}.createByBppSolvableInstances", CLASS_NAME);

    List<BppDetailedSolution> solutions = new ArrayList<>();
    request
            .getSolvableInstancesList()
            .forEach(
                    instance -> {
                      solutions.add(
                              this.createBppDetailedSolutionByBppSolvableInstanceUseCasePort
                                      .execute(
                                              this.bppSolutionGrpcMapper
                                                      .toCreateBppDetailedSolutionByBppSolvableInstanceCommand(instance))
                                      .getDetailedSolution());
                    });

    log.info("[end] {}.createByBppSolvableInstances", CLASS_NAME);

    responseObserver.onNext(
            CreateBppDetailedSolutionByBppSolvableInstancesResponse.newBuilder()
                    .addAllDetailedSolutions(this.bppSolutionGrpcMapper.toBppDetailedSolutionsProto(solutions))
                    .build());
    responseObserver.onCompleted();
  }
}
