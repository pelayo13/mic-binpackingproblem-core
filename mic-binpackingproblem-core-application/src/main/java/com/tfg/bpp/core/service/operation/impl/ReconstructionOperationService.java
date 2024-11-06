package com.tfg.bpp.core.service.operation.impl;

import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppNeighborhoodStructureOperationType;
import com.tfg.bpp.core.model.BppReconstructionOperation;
import com.tfg.bpp.core.service.algorithm.greedy.GreedyAlgorithmServiceFactory;
import com.tfg.bpp.core.service.operation.NeighborhoodStructureOperationService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(BppNeighborhoodStructureOperationType.ServiceName.RECONSTRUCTION)
@Slf4j
@RequiredArgsConstructor
public class ReconstructionOperationService
    implements NeighborhoodStructureOperationService<BppReconstructionOperation> {

  private final GreedyAlgorithmServiceFactory greedyAlgorithmServiceFactory;

  @Override
  public List<BppInstance> apply(List<BppInstance> instances, BppReconstructionOperation operation) {
    List<BppInstance> resultInstances = new ArrayList<>();

    instances.forEach(
        instance -> {
          resultInstances.add(
              this.greedyAlgorithmServiceFactory
                  .getService(operation.getAlgorithm().getServiceName())
                  .getSolution(instance));
        });

    return resultInstances;
  }
}
