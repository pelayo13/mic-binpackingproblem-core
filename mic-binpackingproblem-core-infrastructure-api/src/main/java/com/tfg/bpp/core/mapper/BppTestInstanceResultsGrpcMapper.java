package com.tfg.bpp.core.mapper;

import com.tfg.bpp.core.config.CentralMapperConfig;
import com.tfg.bpp.core.model.BppGreedyAlgorithmType;
import com.tfg.bpp.core.model.BppLocalSearchType;
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
import v1.model.BppLocalSearchTypeProto;
import v1.model.BppTestCaseResultsProto;
import v1.model.BppTestInstanceResultsProto;
import v1.model.BppTestItemsResultsProto;
import v1.model.BppTestResultsProto;
import v1.service.CreateByBppTestInstanceProto.CreateByBppTestInstanceRequest;
import v1.service.CreateByBppTestInstanceProto.CreateByBppTestInstanceResponse;

@Mapper(
    config = CentralMapperConfig.class,
    uses = {GrpcMapper.class})
public interface BppTestInstanceResultsGrpcMapper {

  @Mapping(target = "testInstance.numberItems", source = "testInstance.numberItemsList")
  @Mapping(target = "testInstance.algorithms", source = "testInstance.algorithmsList")
  CreateBppTestInstanceResultsByBppTestInstanceCommand
      toCreateBppTestInstanceResultsByBppTestInstanceCommand(
          CreateByBppTestInstanceRequest request);

  CreateByBppTestInstanceResponse toCreateByBppTestInstanceResponse(
      CreateBppTestInstanceResultsByBppTestInstanceResponse response);

  @Mapping(target = "testItemsResultsList", source = "testItemsResults")
  BppTestInstanceResultsProto.BppTestInstanceResults toBppTestInstanceResultsProto(
      BppTestInstanceResults testInstanceResults);

  @Mapping(target = "testResultsList", source = "testResults")
  BppTestItemsResultsProto.BppTestItemsResults toBppTestInstanceResultsProto(
      BppTestItemsResults testInstanceResults);

  @Mapping(target = "testCaseResultsList", source = "testCaseResults")
  BppTestResultsProto.BppTestResults toBppTestResultsProto(BppTestResults bppTestResults);

  BppTestCaseResultsProto.BppTestCaseResults toBppTestCaseResultsProto(
      BppTestCaseResults bppTestCaseResults);

  @ValueMapping(target = "RANDOM", source = "BPP_GREEDY_ALGORITHM_TYPE_RANDOM")
  @ValueMapping(
      target = "FIRST_FIT_DECREASING",
      source = "BPP_GREEDY_ALGORITHM_TYPE_FIRST_FIT_DECREASING")
  @ValueMapping(
          target = "BEST_FIT_DECREASING",
          source = "BPP_GREEDY_ALGORITHM_TYPE_BEST_FIT_DECREASING")
  @ValueMapping(target = MappingConstants.NULL, source = "BPP_GREEDY_ALGORITHM_TYPE_UNSPECIFIED")
  @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = "UNRECOGNIZED")
  BppGreedyAlgorithmType toBppGreedyAlgorithmType(
      BppGreedyAlgorithmTypeProto.BppGreedyAlgorithmType bppGreedyAlgorithmTypeProto);

  @ValueMapping(target = "ALVIN_ET_AL", source = "BPP_LOCAL_SEARCH_TYPE_ALVIN_ET_AL")
  @ValueMapping(target = MappingConstants.NULL, source = "BPP_LOCAL_SEARCH_TYPE_UNSPECIFIED")
  @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = "UNRECOGNIZED")
  BppLocalSearchType toBppLocalSearchType(
      BppLocalSearchTypeProto.BppLocalSearchType bppLocalSearchTypeProto);

  @ValueMapping(target = "BPP_GREEDY_ALGORITHM_TYPE_RANDOM", source = "RANDOM")
  @ValueMapping(
      target = "BPP_GREEDY_ALGORITHM_TYPE_FIRST_FIT_DECREASING",
      source = "FIRST_FIT_DECREASING")
  @ValueMapping(
          target = "BPP_GREEDY_ALGORITHM_TYPE_BEST_FIT_DECREASING",
          source = "BEST_FIT_DECREASING")
  @ValueMapping(
      target = "BPP_GREEDY_ALGORITHM_TYPE_UNSPECIFIED",
      source = MappingConstants.ANY_REMAINING)
  BppGreedyAlgorithmTypeProto.BppGreedyAlgorithmType toBppGreedyAlgorithmTypeProto(
      BppGreedyAlgorithmType bppGreedyAlgorithmType);

  @ValueMapping(target = "BPP_LOCAL_SEARCH_TYPE_ALVIN_ET_AL", source = "ALVIN_ET_AL")
  @ValueMapping(
      target = "BPP_LOCAL_SEARCH_TYPE_UNSPECIFIED",
      source = MappingConstants.ANY_REMAINING)
  BppLocalSearchTypeProto.BppLocalSearchType toBppLocalSearchTypeProto(
      BppLocalSearchType bppLocalSearchType);
}
