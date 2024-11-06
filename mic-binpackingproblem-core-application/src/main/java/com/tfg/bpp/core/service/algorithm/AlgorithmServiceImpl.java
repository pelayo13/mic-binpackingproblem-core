package com.tfg.bpp.core.service.algorithm;

import com.tfg.bpp.core.mapper.BppInstanceMapper;
import com.tfg.bpp.core.model.BppGreedyAlgorithmType;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppLocalSearchAlgorithm;
import com.tfg.bpp.core.model.BppLocalSearchSolution;
import com.tfg.bpp.core.service.algorithm.greedy.GreedyAlgorithmServiceFactory;
import com.tfg.bpp.core.service.algorithm.localsearch.LocalSearchService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlgorithmServiceImpl implements AlgorithmService {

  private final GreedyAlgorithmServiceFactory greedyAlgorithmServiceFactory;

  private final LocalSearchService localSearchService;

  private final BppInstanceMapper bppInstanceMapper;

  @Override
  public BppInstance getSolution(BppInstance instance, BppGreedyAlgorithmType greedyAlgorithmType) {
    Optional<BppInstance> solution =
        Optional.ofNullable(greedyAlgorithmType)
            .map(
                bppGreedyAlgorithmType ->
                    this.greedyAlgorithmServiceFactory.getService(
                        bppGreedyAlgorithmType.getServiceName()))
            .map(algorithmService -> algorithmService.getSolution(new BppInstance(instance)));

    if (solution.isPresent()) {
      return solution.get();
    } else {
      BppInstance updatedSolution = new BppInstance(instance);
      updatedSolution.recalculateAllMetrics();
      return updatedSolution;
    }
  }

  public BppLocalSearchSolution getLocalSearchSolution(
      BppInstance solution, BppLocalSearchAlgorithm localSearchAlgorithm) {
    return Optional.ofNullable(localSearchAlgorithm)
        .map(
            localSearchService ->
                this.localSearchService.getSolution(solution, localSearchAlgorithm))
        .orElse(this.bppInstanceMapper.toBppLocalSearchSolution(solution));
  }
}
