package com.tfg.bpp.core.service.operation.impl;

import com.tfg.bpp.core.model.BppBinsDestructionOperation;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppNeighborhoodStructureOperationType;
import com.tfg.bpp.core.service.operation.NeighborhoodStructureOperationService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(BppNeighborhoodStructureOperationType.ServiceName.BINS_DESTRUCTION)
@Slf4j
@RequiredArgsConstructor
public class BinsDestructionOperationService
    implements NeighborhoodStructureOperationService<BppBinsDestructionOperation> {

  @Override
  public List<BppInstance> apply(
      List<BppInstance> instances, BppBinsDestructionOperation operation) {
    List<BppInstance> resultInstances = new ArrayList<>();

    instances.forEach(
        instance -> {
          List<Integer> indexOfBinsToDestroy =
              operation
                  .getBinsSelectionFunction()
                  .getSelection(instance.getBins(), operation.getNumberToDestroy());
          instance.removeBins(indexOfBinsToDestroy);
          resultInstances.add(instance);
        });

    return resultInstances;
  }
}
