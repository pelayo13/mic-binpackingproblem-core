package com.tfg.bpp.core.service.algorithm.localsearch;

import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppLocalSearchAlgorithm;
import com.tfg.bpp.core.model.BppLocalSearchSolution;
import com.tfg.bpp.core.model.BppStrategyControlResult;
import com.tfg.bpp.core.service.operation.NeighborhoodStructureOperationServiceFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocalSearchServiceImpl implements LocalSearchService {

  private final NeighborhoodStructureOperationServiceFactory
      neighborhoodStructureOperationServiceFactory;

  @Override
  public BppLocalSearchSolution getSolution(
      BppInstance bppInstance, BppLocalSearchAlgorithm localSearchAlgorithm) {
    BppInstance previousSolution = new BppInstance(bppInstance);
    final BppInstance originOfNeighbors = previousSolution;
    ArrayList<Double> evaluationFunctionResultsRecord = new ArrayList<>();
    int numberNeighborsGenerated = 0;
    int numberOfIterations = 0;
    int numberEvaluatedNeighbors = 0;
    evaluationFunctionResultsRecord.add(
        localSearchAlgorithm.getBppEvaluationFunction().getValue(bppInstance));

    while (numberOfIterations
        < localSearchAlgorithm.getBppStopCriteria().getMaxNumberIterations()) {
      numberOfIterations++;
      List<BppInstance> finalNeighbors =
          localSearchAlgorithm.getBppNeighborhoodStructures().stream()
              .map(
                  bppNeighborhoodStructure ->
                      bppNeighborhoodStructure.getBppNeighborhoodStructureOperations().stream()
                          .reduce(
                              List.of(new BppInstance(originOfNeighbors)),
                              (neighbors, neighborOperation) ->
                                  this.neighborhoodStructureOperationServiceFactory
                                      .getService(neighborOperation.getType().getServiceName())
                                      .apply(neighbors, neighborOperation),
                              (neighbors1, neighbors2) -> neighbors2))
              .flatMap(Collection::stream)
              .collect(Collectors.toList());

      BppStrategyControlResult bppStrategyControlResult =
          localSearchAlgorithm
              .getBppStrategyControl()
              .getNewSolution(
                  previousSolution,
                  finalNeighbors,
                  localSearchAlgorithm.getBppEvaluationFunction());

      BppInstance newSolution = bppStrategyControlResult.getNewSolution();
      numberNeighborsGenerated = numberNeighborsGenerated + finalNeighbors.size();
      numberEvaluatedNeighbors =
          numberEvaluatedNeighbors + bppStrategyControlResult.getNumberEvaluatedNeighbors();

      if (newSolution.equals(previousSolution)) {
        return BppLocalSearchSolution.builder()
            .bins(newSolution.getBins())
            .items(newSolution.getItems())
            .binsCapacity(newSolution.getBinsCapacity())
            .numberIterations(numberOfIterations)
            .evaluationFunctionResultsRecords(evaluationFunctionResultsRecord)
            .numberNeighborsGenerated(numberNeighborsGenerated)
            .numberEvaluatedNeighbors(numberEvaluatedNeighbors)
            .build();
      } else {
        evaluationFunctionResultsRecord.add(
            localSearchAlgorithm.getBppEvaluationFunction().getValue(newSolution));
        previousSolution = bppStrategyControlResult.getNewSolution();
      }
    }

    return BppLocalSearchSolution.builder()
        .bins(previousSolution.getBins())
        .items(previousSolution.getItems())
        .binsCapacity(previousSolution.getBinsCapacity())
        .numberIterations(numberOfIterations)
        .evaluationFunctionResultsRecords(evaluationFunctionResultsRecord)
        .numberNeighborsGenerated(numberNeighborsGenerated)
        .numberEvaluatedNeighbors(numberEvaluatedNeighbors)
        .build();
  }
}
