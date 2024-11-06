package com.tfg.bpp.core.mapper;

import com.tfg.bpp.core.config.CentralMapperConfig;
import com.tfg.bpp.core.model.BppBinsDestructionOperation;
import com.tfg.bpp.core.model.BppBinsInterchangeOperation;
import com.tfg.bpp.core.model.BppBinsSelectionFunction;
import com.tfg.bpp.core.model.BppEvaluationFunction;
import com.tfg.bpp.core.model.BppGreedyAlgorithmType;
import com.tfg.bpp.core.model.BppItemsInterchangeFunction;
import com.tfg.bpp.core.model.BppItemsSelectionFunction;
import com.tfg.bpp.core.model.BppLocalSearchAlgorithm;
import com.tfg.bpp.core.model.BppNeighborhoodStructure;
import com.tfg.bpp.core.model.BppNeighborhoodStructureOperation;
import com.tfg.bpp.core.model.BppNeighborhoodStructureOperationType;
import com.tfg.bpp.core.model.BppReconstructionOperation;
import com.tfg.bpp.core.model.BppStoredItemsAndFreeItemsInterchangeOperation;
import com.tfg.bpp.core.model.BppStoredItemsInterchangeOperation;
import com.tfg.bpp.core.model.BppStrategyControl;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ValueMapping;
import v1.model.BppBinsDestructionOperationProto;
import v1.model.BppBinsInterchangeOperationProto;
import v1.model.BppBinsSelectionFunctionProto;
import v1.model.BppEvaluationFunctionProto;
import v1.model.BppGreedyAlgorithmTypeProto;
import v1.model.BppItemsInterchangeFunctionProto;
import v1.model.BppItemsSelectionFunctionProto;
import v1.model.BppLocalSearchAlgorithmProto;
import v1.model.BppNeighborhoodStructureOperationProto;
import v1.model.BppNeighborhoodStructureOperationTypeProto;
import v1.model.BppNeighborhoodStructureProto;
import v1.model.BppReconstructionOperationProto;
import v1.model.BppStoredItemsAndFreeItemsInterchangeOperationProto;
import v1.model.BppStoredItemsInterchangeOperationProto;
import v1.model.BppStrategyControlProto;

@Mapper(
    config = CentralMapperConfig.class,
    uses = {GrpcMapper.class})
public interface BppAlgorithmGrpcMapper {

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

  @Mapping(
      target = "bppNeighborhoodStructures",
      source = "bppNeighborhoodStructuresList")
  BppLocalSearchAlgorithm toBppLocalSearch(
      BppLocalSearchAlgorithmProto.BppLocalSearchAlgorithm bppLocalSearchAlgorithmProto);

  @Mapping(
      target = "bppNeighborhoodStructureOperations",
      source = "bppNeighborhoodStructureOperationsList",
      qualifiedByName = "toBppNeighborhoodStructureOperation")
  BppNeighborhoodStructure toBppNeighborhoodStructure(
      BppNeighborhoodStructureProto.BppNeighborhoodStructure bppNeighborhoodStructureProto);

  @ValueMapping(target = "HILL_CLIMBING", source = "BPP_STRATEGY_CONTROL_HILL_CLIMBING")
  @ValueMapping(target = MappingConstants.NULL, source = "BPP_STRATEGY_CONTROL_UNSPECIFIED")
  @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = "UNRECOGNIZED")
  BppStrategyControl toBppStrategyControl(
      BppStrategyControlProto.BppStrategyControl bppStrategyControlProto);

  @ValueMapping(target = "BINS_NUMBER", source = "BPP_EVALUATION_FUNCTION_BINS_NUMBER")
  @ValueMapping(target = "TARDINESS", source = "BPP_EVALUATION_FUNCTION_TARDINESS")
  @ValueMapping(target = "LATENESS", source = "BPP_EVALUATION_FUNCTION_LATENESS")
  @ValueMapping(target = "MAXIMUM_LATENESS", source = "BPP_EVALUATION_FUNCTION_MAXIMUM_LATENESS")
  @ValueMapping(
      target = "AVAILABLE_CAPACITY",
      source = "BPP_EVALUATION_FUNCTION_AVAILABLE_CAPACITY")
  @ValueMapping(target = "FITNESS", source = "BPP_EVALUATION_FUNCTION_FITNESS")
  @ValueMapping(target = MappingConstants.NULL, source = "BPP_EVALUATION_FUNCTION_UNSPECIFIED")
  @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = "UNRECOGNIZED")
  BppEvaluationFunction toBppEvaluationFunction(
      BppEvaluationFunctionProto.BppEvaluationFunction evaluationFunctionProto);

  @Named("toBppNeighborhoodStructureOperation")
  default BppNeighborhoodStructureOperation toBppNeighborhoodStructureOperation(
      BppNeighborhoodStructureOperationProto.BppNeighborhoodStructureOperation
          bppNeighborhoodStructureOperationProto) {
    switch (bppNeighborhoodStructureOperationProto.getType()) {
      case BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_BINS_DESTRUCTION -> {
        return this.toBppBinsDestructionOperation(bppNeighborhoodStructureOperationProto);
      }
      case BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_RECONSTRUCTION -> {
        return this.toBppReconstructionOperation(bppNeighborhoodStructureOperationProto);
      }
      case BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_BINS_INTERCHANGE -> {
        return this.toBppBinsInterchangeOperation(bppNeighborhoodStructureOperationProto);
      }
      case BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_STORED_ITEMS_AND_FREE_ITEMS_INTERCHANGE -> {
        return this.toBppStoredItemsAndFreeItemsInterchangeOperation(
            bppNeighborhoodStructureOperationProto);
      }
      case BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_STORED_ITEMS_INTERCHANGE -> {
        return this.toBppStoredItemsInterchangeOperation(bppNeighborhoodStructureOperationProto);
      }
      default -> {
        return null;
      }
    }
  }

  @Mapping(target = ".", source = "binsDestructionOperation")
  BppBinsDestructionOperation toBppBinsDestructionOperation(
      BppNeighborhoodStructureOperationProto.BppNeighborhoodStructureOperation
          bppNeighborhoodStructureOperationProto);

  @Mapping(target = ".", source = "reconstructionOperation")
  BppReconstructionOperation toBppReconstructionOperation(
      BppNeighborhoodStructureOperationProto.BppNeighborhoodStructureOperation
          bppNeighborhoodStructureOperationProto);

  @Mapping(target = ".", source = "binsInterchangeOperation")
  BppBinsInterchangeOperation toBppBinsInterchangeOperation(
      BppNeighborhoodStructureOperationProto.BppNeighborhoodStructureOperation
          bppNeighborhoodStructureOperationProto);

  @Mapping(target = ".", source = "storedItemsAndFreeItemsInterchangeOperation")
  BppStoredItemsAndFreeItemsInterchangeOperation toBppStoredItemsAndFreeItemsInterchangeOperation(
      BppNeighborhoodStructureOperationProto.BppNeighborhoodStructureOperation
          bppNeighborhoodStructureOperationProto);

  @Mapping(target = ".", source = "storedItemsInterchangeOperation")
  BppStoredItemsInterchangeOperation toBppStoredItemsInterchangeOperation(
      BppNeighborhoodStructureOperationProto.BppNeighborhoodStructureOperation
          bppNeighborhoodStructureOperationProto);

  @ValueMapping(
      target = "SMALLEST_OCCUPIED_CAPACITY",
      source = "BPP_BINS_SELECTION_FUNCTION_SMALLEST_OCCUPIED_CAPACITY")
  @ValueMapping(target = MappingConstants.NULL, source = "BPP_BINS_SELECTION_FUNCTION_UNSPECIFIED")
  @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = "UNRECOGNIZED")
  BppBinsSelectionFunction toBppBinsSelectionFunction(
      BppBinsSelectionFunctionProto.BppBinsSelectionFunction binsSelectionFunctionProto);

  @ValueMapping(target = "BIGGEST_SIZE", source = "BPP_ITEMS_SELECTION_FUNCTION_BIGGEST_SIZE")
  @ValueMapping(target = MappingConstants.NULL, source = "BPP_ITEMS_SELECTION_FUNCTION_UNSPECIFIED")
  @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = "UNRECOGNIZED")
  BppItemsSelectionFunction toBppItemsSelectionFunction(
      BppItemsSelectionFunctionProto.BppItemsSelectionFunction bppItemsSelectionFunctionProto);

  @ValueMapping(target = "SIZE", source = "BPP_ITEMS_INTERCHANGE_FUNCTION_SIZE")
  @ValueMapping(target = "ALWAYS", source = "BPP_ITEMS_INTERCHANGE_FUNCTION_ALWAYS")
  @ValueMapping(
      target = MappingConstants.NULL,
      source = "BPP_ITEMS_INTERCHANGE_FUNCTION_UNSPECIFIED")
  @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = "UNRECOGNIZED")
  BppItemsInterchangeFunction toBppItemsInterchangeFunction(
      BppItemsInterchangeFunctionProto.BppItemsInterchangeFunction
          bppItemsInterchangeFunctionProto);

  @ValueMapping(
      target = "BINS_DESTRUCTION",
      source = "BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_BINS_DESTRUCTION")
  @ValueMapping(
      target = "RECONSTRUCTION",
      source = "BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_RECONSTRUCTION")
  @ValueMapping(
      target = "BINS_INTERCHANGE",
      source = "BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_BINS_INTERCHANGE")
  @ValueMapping(
      target = "STORED_ITEMS_AND_FREE_ITEMS_INTERCHANGE",
      source = "BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_STORED_ITEMS_AND_FREE_ITEMS_INTERCHANGE")
  @ValueMapping(
      target = "STORED_ITEMS_INTERCHANGE",
      source = "BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_STORED_ITEMS_INTERCHANGE")
  @ValueMapping(
      target = MappingConstants.NULL,
      source = "BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_UNSPECIFIED")
  @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = "UNRECOGNIZED")
  BppNeighborhoodStructureOperationType toBppNeighborhoodStructureOperationType(
      BppNeighborhoodStructureOperationTypeProto.BppNeighborhoodStructureOperationType typeProto);

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

  @Mapping(target = "bppNeighborhoodStructuresList", source = "bppNeighborhoodStructures")
  BppLocalSearchAlgorithmProto.BppLocalSearchAlgorithm toBppLocalSearchProto(
      BppLocalSearchAlgorithm bppLocalSearchAlgorithm);

  @Mapping(
      target = "bppNeighborhoodStructureOperationsList",
      source = "bppNeighborhoodStructureOperations")
  BppNeighborhoodStructureProto.BppNeighborhoodStructure toBppNeighborhoodStructureProto(
      BppNeighborhoodStructure bppNeighborhoodStructure);

  @ValueMapping(target = "BPP_STRATEGY_CONTROL_HILL_CLIMBING", source = "HILL_CLIMBING")
  @ValueMapping(
      target = "BPP_STRATEGY_CONTROL_UNSPECIFIED",
      source = MappingConstants.ANY_REMAINING)
  BppStrategyControlProto.BppStrategyControl toBppStrategyControlProto(
      BppStrategyControl bppStrategyControl);

  @ValueMapping(target = "BPP_EVALUATION_FUNCTION_BINS_NUMBER", source = "BINS_NUMBER")
  @ValueMapping(target = "BPP_EVALUATION_FUNCTION_TARDINESS", source = "TARDINESS")
  @ValueMapping(target = "BPP_EVALUATION_FUNCTION_LATENESS", source = "LATENESS")
  @ValueMapping(target = "BPP_EVALUATION_FUNCTION_MAXIMUM_LATENESS", source = "MAXIMUM_LATENESS")
  @ValueMapping(
      target = "BPP_EVALUATION_FUNCTION_AVAILABLE_CAPACITY",
      source = "AVAILABLE_CAPACITY")
  @ValueMapping(target = "BPP_EVALUATION_FUNCTION_FITNESS", source = "FITNESS")
  @ValueMapping(
      target = "BPP_EVALUATION_FUNCTION_UNSPECIFIED",
      source = MappingConstants.ANY_REMAINING)
  BppEvaluationFunctionProto.BppEvaluationFunction toBppEvaluationFunctionProto(
      BppEvaluationFunction evaluationFunction);

  default BppNeighborhoodStructureOperationProto.BppNeighborhoodStructureOperation
      toBppNeighborhoodStructureOperationProto(
          BppNeighborhoodStructureOperation bppNeighborhoodStructureOperation) {
    if (bppNeighborhoodStructureOperation == null) {
      return null;
    }

    BppNeighborhoodStructureOperationProto.BppNeighborhoodStructureOperation.Builder
        bppNeighborhoodStructureOperation1 =
            BppNeighborhoodStructureOperationProto.BppNeighborhoodStructureOperation.newBuilder();

    if (bppNeighborhoodStructureOperation.getType() != null) {
      bppNeighborhoodStructureOperation1.setType(
          toBppNeighborhoodStructureOperationTypeProto(
              bppNeighborhoodStructureOperation.getType()));

      switch (bppNeighborhoodStructureOperation.getType()) {
        case BINS_DESTRUCTION ->
            bppNeighborhoodStructureOperation1.setBinsDestructionOperation(
                this.toBppBinsDestructionOperationProto(
                    (BppBinsDestructionOperation) bppNeighborhoodStructureOperation));
        case RECONSTRUCTION ->
            bppNeighborhoodStructureOperation1.setReconstructionOperation(
                this.toBppReconstructionOperationProto(
                    (BppReconstructionOperation) bppNeighborhoodStructureOperation));
        case BINS_INTERCHANGE ->
            bppNeighborhoodStructureOperation1.setBinsInterchangeOperation(
                this.toBppBinsInterchangeOperationProto(
                    (BppBinsInterchangeOperation) bppNeighborhoodStructureOperation));
        case STORED_ITEMS_AND_FREE_ITEMS_INTERCHANGE ->
            bppNeighborhoodStructureOperation1.setStoredItemsAndFreeItemsInterchangeOperation(
                this.toBppStoredItemsAndFreeItemsInterchangeOperationProto(
                    (BppStoredItemsAndFreeItemsInterchangeOperation)
                        bppNeighborhoodStructureOperation));
        case STORED_ITEMS_INTERCHANGE ->
            bppNeighborhoodStructureOperation1.setStoredItemsInterchangeOperation(
                this.toBppStoredItemsInterchangeOperationProto(
                    (BppStoredItemsInterchangeOperation) bppNeighborhoodStructureOperation));
      }
    }

    return bppNeighborhoodStructureOperation1.build();
  }

  BppBinsDestructionOperationProto.BppBinsDestructionOperation toBppBinsDestructionOperationProto(
      BppBinsDestructionOperation bppBinsDestructionOperation);

  BppReconstructionOperationProto.BppReconstructionOperation toBppReconstructionOperationProto(
      BppReconstructionOperation bppReconstructionOperation);

  BppBinsInterchangeOperationProto.BppBinsInterchangeOperation toBppBinsInterchangeOperationProto(
      BppBinsInterchangeOperation bppBinsInterchangeOperation);

  BppStoredItemsAndFreeItemsInterchangeOperationProto.BppStoredItemsAndFreeItemsInterchangeOperation
      toBppStoredItemsAndFreeItemsInterchangeOperationProto(
          BppStoredItemsAndFreeItemsInterchangeOperation
              bppStoredItemsAndFreeItemsInterchangeOperation);

  BppStoredItemsInterchangeOperationProto.BppStoredItemsInterchangeOperation
      toBppStoredItemsInterchangeOperationProto(
          BppStoredItemsInterchangeOperation bppStoredItemsInterchangeOperation);

  @ValueMapping(
      target = "BPP_BINS_SELECTION_FUNCTION_SMALLEST_OCCUPIED_CAPACITY",
      source = "SMALLEST_OCCUPIED_CAPACITY")
  @ValueMapping(
      target = "BPP_BINS_SELECTION_FUNCTION_UNSPECIFIED",
      source = MappingConstants.ANY_REMAINING)
  BppBinsSelectionFunctionProto.BppBinsSelectionFunction toBppBinsSelectionFunctionProto(
      BppBinsSelectionFunction binsSelectionFunction);

  @ValueMapping(target = "BPP_ITEMS_SELECTION_FUNCTION_BIGGEST_SIZE", source = "BIGGEST_SIZE")
  @ValueMapping(
      target = "BPP_ITEMS_SELECTION_FUNCTION_UNSPECIFIED",
      source = MappingConstants.ANY_REMAINING)
  BppItemsSelectionFunctionProto.BppItemsSelectionFunction toBppItemsSelectionFunctionProto(
      BppItemsSelectionFunction bppItemsSelectionFunction);

  @ValueMapping(target = "BPP_ITEMS_INTERCHANGE_FUNCTION_SIZE", source = "SIZE")
  @ValueMapping(target = "BPP_ITEMS_INTERCHANGE_FUNCTION_ALWAYS", source = "ALWAYS")
  @ValueMapping(
      target = "BPP_ITEMS_INTERCHANGE_FUNCTION_UNSPECIFIED",
      source = MappingConstants.ANY_REMAINING)
  BppItemsInterchangeFunctionProto.BppItemsInterchangeFunction toBppItemsInterchangeFunctionProto(
      BppItemsInterchangeFunction bppItemsInterchangeFunction);

  @ValueMapping(
      target = "BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_BINS_DESTRUCTION",
      source = "BINS_DESTRUCTION")
  @ValueMapping(
      target = "BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_RECONSTRUCTION",
      source = "RECONSTRUCTION")
  @ValueMapping(
      target = "BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_BINS_INTERCHANGE",
      source = "BINS_INTERCHANGE")
  @ValueMapping(
      target = "BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_STORED_ITEMS_AND_FREE_ITEMS_INTERCHANGE",
      source = "STORED_ITEMS_AND_FREE_ITEMS_INTERCHANGE")
  @ValueMapping(
      target = "BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_STORED_ITEMS_INTERCHANGE",
      source = "STORED_ITEMS_INTERCHANGE")
  @ValueMapping(
      target = "BPP_NEIGHBORHOOD_STRUCTURE_OPERATION_TYPE_UNSPECIFIED",
      source = MappingConstants.ANY_REMAINING)
  BppNeighborhoodStructureOperationTypeProto.BppNeighborhoodStructureOperationType
      toBppNeighborhoodStructureOperationTypeProto(BppNeighborhoodStructureOperationType type);
}
