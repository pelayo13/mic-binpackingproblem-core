package com.tfg.bpp.core.mapper;

import com.tfg.bpp.core.config.CentralMapperConfig;
import com.tfg.bpp.core.model.BppBin;
import com.tfg.bpp.core.model.BppDetailedSolution;
import com.tfg.bpp.core.model.BppGreedyAlgorithmType;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppItem;
import com.tfg.bpp.core.model.BppLocalSearchType;
import com.tfg.bpp.core.model.BppSolution;
import com.tfg.bpp.core.model.BppStoredItem;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppDetailedSolutionByBppSolvableInstanceUseCasePort.CreateBppDetailedSolutionByBppSolvableInstanceCommand;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppSolutionByBppSolvableInstanceUseCasePort.CreateBppSolutionByBppSolvableInstanceCommand;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import v1.model.BppBinProto;
import v1.model.BppDetailedSolutionProto;
import v1.model.BppGreedyAlgorithmTypeProto;
import v1.model.BppInstanceProto;
import v1.model.BppItemProto;
import v1.model.BppLocalSearchTypeProto;
import v1.model.BppSolutionProto;
import v1.model.BppSolvableInstanceProto;
import v1.model.BppStoredItemProto;

@Mapper(
    config = CentralMapperConfig.class,
    uses = {GrpcMapper.class})
public interface BppSolutionGrpcMapper {

  @Mapping(target = "solvableInstance", source = ".")
  CreateBppSolutionByBppSolvableInstanceCommand toCreateBppSolutionByBppSolvableInstanceCommand(
      BppSolvableInstanceProto.BppSolvableInstance bppSolvableInstance);

  @Mapping(target = "solvableInstance", source = ".")
  CreateBppDetailedSolutionByBppSolvableInstanceCommand toCreateBppDetailedSolutionByBppSolvableInstanceCommand(
          BppSolvableInstanceProto.BppSolvableInstance bppSolvableInstance);

  @Mapping(target = "binsList", source = "bins")
  List<BppSolutionProto.BppSolution> toBppSolutionsProto(List<BppSolution> bppSolutions);

  List<BppDetailedSolutionProto.BppDetailedSolution> toBppDetailedSolutionsProto(List<BppDetailedSolution> bppDetailedSolutionsProto);

  @Mapping(target = "recordInstancesList", source = "recordInstances")
  BppDetailedSolutionProto.BppDetailedSolution toBppDetailedSolutionsProto(BppDetailedSolution bppDetailedSolutionsProto);

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

  @Mapping(target = "items", source = "itemsList")
  @Mapping(target = "bins", source = "binsList")
  BppInstance toBppInstance(BppInstanceProto.BppInstance bppInstanceProto);

  @Mapping(target = "itemsList", source = "items")
  @Mapping(target = "binsList", source = "bins")
  BppInstanceProto.BppInstance toBppInstanceProto(BppInstance bppInstanceProto);

  BppItem toBppItem(BppItemProto.BppItem bppItem);

  @Mapping(target = "items", source = "itemsList")
  BppBin toBppBin(BppBinProto.BppBin bppBin);

  @Mapping(target = ".", source = "item")
  BppStoredItem toBppStoredItem(BppStoredItemProto.BppStoredItem bppStoredItem);

  @Mapping(target = "itemsList", source = "items")
  BppBinProto.BppBin toBppBinProto(BppBin bppBin);

  @Mapping(target = "item", source = ".")
  BppStoredItemProto.BppStoredItem toBppStoredItemProto(BppStoredItem bppStoredItem);

  @Mapping(target = "binsList", source = "bins")
  BppSolutionProto.BppSolution toBppSolutionProto(BppSolution bppSolution);
}