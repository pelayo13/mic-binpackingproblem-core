package com.tfg.bpp.core.mapper;

import com.tfg.bpp.core.config.CentralMapperConfig;
import com.tfg.bpp.core.model.BppBin;
import com.tfg.bpp.core.model.BppDetailsOfSolution;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppItem;
import com.tfg.bpp.core.model.BppSolution;
import com.tfg.bpp.core.port.inbound.usecase.CreateBppSolutionsByBppSolvableInstanceUseCasePort;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import v1.model.BppBinProto;
import v1.model.BppDetailsOfSolutionProto;
import v1.model.BppInstanceProto;
import v1.model.BppItemProto;
import v1.model.BppSolutionProto;
import v1.model.BppSolvableInstanceProto;

@Mapper(
    config = CentralMapperConfig.class,
    uses = {GrpcMapper.class, BppAlgorithmGrpcMapper.class})
public interface BppInstanceControllerGrpcMapper {

  @Mapping(target = "solvableInstance", source = ".")
  CreateBppSolutionsByBppSolvableInstanceUseCasePort.CreateBppSolutionsByBppSolvableInstanceCommand toCreateBppSolutionByBppSolvableInstanceCommand(
      BppSolvableInstanceProto.BppSolvableInstance bppSolvableInstance);

  @Mapping(target = "binsList", source = "bins")
  List<BppSolutionProto.BppSolution> toBppSolutionsProto(List<BppSolution> bppSolutions);

  @Mapping(target = "recordInstancesList", source = "recordInstances")
  BppDetailsOfSolutionProto.BppDetailsOfSolution toBppDetailsOfSolutionProto(BppDetailsOfSolution bppDetailsOfSolution);

  @Mapping(target = "items", source = "itemsList")
  @Mapping(target = "bins", source = "binsList")
  BppInstance toBppInstance(BppInstanceProto.BppInstance bppInstanceProto);

  @Mapping(target = "itemsList", source = "items")
  @Mapping(target = "binsList", source = "bins")
  BppInstanceProto.BppInstance toBppInstanceProto(BppInstance bppInstanceProto);

  BppItem toBppItem(BppItemProto.BppItem bppItem);

  @Mapping(target = "items", source = "itemsList")
  BppBin toBppBin(BppBinProto.BppBin bppBin);

  @Mapping(target = "itemsList", source = "items")
  BppBinProto.BppBin toBppBinProto(BppBin bppBin);

  @Mapping(target = "binsList", source = "bins")
  BppSolutionProto.BppSolution toBppSolutionProto(BppSolution bppSolution);
}
