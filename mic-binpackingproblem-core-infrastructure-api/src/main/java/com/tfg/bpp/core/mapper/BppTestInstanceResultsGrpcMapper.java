package com.tfg.bpp.core.mapper;

import com.tfg.bpp.core.config.CentralMapperConfig;
import com.tfg.bpp.core.model.BppGreedyAlgorithmType;
import com.tfg.bpp.core.model.BppTestCaseResults;
import com.tfg.bpp.core.model.BppTestInstanceResults;
import com.tfg.bpp.core.model.BppTestItemsResults;
import com.tfg.bpp.core.model.BppTestResults;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppTestInstanceResultsByBppTestInstanceUseCasePort.CreateBppTestInstanceResultsByBppTestInstanceCommand;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppTestInstanceResultsByBppTestInstanceUseCasePort.CreateBppTestInstanceResultsByBppTestInstanceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import v1.model.BppGreedyAlgorithmTypeProto;
import v1.model.BppTestCaseResultsProto;
import v1.model.BppTestInstanceResultsProto;
import v1.model.BppTestItemsResultsProto;
import v1.model.BppTestResultsProto;
import v1.service.CreateByBppInstanceProto;
import v1.service.CreateByBppTestInstanceProto.CreateByBppTestInstanceRequest;
import v1.service.CreateByBppTestInstanceProto.CreateByBppTestInstanceResponse;

import java.util.List;

@Mapper(
    config = CentralMapperConfig.class,
    uses = {GrpcMapper.class, BppAlgorithmGrpcMapper.class})
public interface BppTestInstanceResultsGrpcMapper {

  @Mapping(target = "testInstance.numberItems", source = "testInstance.numberItemsList")
  @Mapping(target = "testInstance.algorithms", source = "testInstance.algorithmsList")
  CreateBppTestInstanceResultsByBppTestInstanceCommand
      toCreateBppTestInstanceResultsByBppTestInstanceCommand(
          CreateByBppTestInstanceRequest request);

  CreateByBppTestInstanceResponse toCreateByBppTestInstanceResponse(
      CreateBppTestInstanceResultsByBppTestInstanceResponse response);

  List<BppTestResultsProto.BppTestResults> toBppTestResultsListProto(List<BppTestResults> bppTestResults);

  @Mapping(target = "testItemsResultsList", source = "testItemsResults")
  BppTestInstanceResultsProto.BppTestInstanceResults toBppTestInstanceResultsProto(
      BppTestInstanceResults testInstanceResults);

  @Mapping(target = "testResultsList", source = "testResults")
  BppTestItemsResultsProto.BppTestItemsResults toBppTestInstanceResultsProto(
      BppTestItemsResults testInstanceResults);

  @Mapping(target = "testCaseResultsList", source = "testCaseResults")
  BppTestResultsProto.BppTestResults toBppTestResultsProto(BppTestResults bppTestResults);

  @Mapping(target = "evaluationFunctionResultsRecordsList", source = "evaluationFunctionResultsRecords")
  BppTestCaseResultsProto.BppTestCaseResults toBppTestCaseResultsProto(
      BppTestCaseResults bppTestCaseResults);
}
