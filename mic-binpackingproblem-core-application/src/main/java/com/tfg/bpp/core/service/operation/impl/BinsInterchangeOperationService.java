package com.tfg.bpp.core.service.operation.impl;

import com.tfg.bpp.core.model.BppBin;
import com.tfg.bpp.core.model.BppBinsInterchangeOperation;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppNeighborhoodStructureOperationType;
import com.tfg.bpp.core.service.operation.NeighborhoodStructureOperationService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(BppNeighborhoodStructureOperationType.ServiceName.BINS_INTERCHANGE)
@Slf4j
@RequiredArgsConstructor
public class BinsInterchangeOperationService
    implements NeighborhoodStructureOperationService<BppBinsInterchangeOperation> {

  @Override
  public List<BppInstance> apply(
      List<BppInstance> instances, BppBinsInterchangeOperation operation) {
    List<BppInstance> resultInstances = new ArrayList<>();

    instances.forEach(
        instance -> {
          IntStream.range(0, instance.getBins().size())
              .forEach(
                  indexFirstBinToInterchange ->
                      IntStream.range(indexFirstBinToInterchange + 1, instance.getBins().size())
                          .forEach(
                              indexSecondBinToInterchange -> {
                                List<BppBin> bins = new ArrayList<>(instance.getBins());
                                bins.set(
                                    indexSecondBinToInterchange,
                                    new BppBin(instance.getBins().get(indexFirstBinToInterchange)));
                                bins.set(
                                    indexFirstBinToInterchange,
                                    new BppBin(instance.getBins().get(indexSecondBinToInterchange)));
                                bins.get(indexFirstBinToInterchange)
                                    .setFirstUseInstant(indexFirstBinToInterchange);
                                bins.get(indexSecondBinToInterchange)
                                    .setFirstUseInstant(indexSecondBinToInterchange);

                                BppInstance neighbor =
                                    BppInstance.builder()
                                        .bins(bins)
                                        .binsCapacity(instance.getBinsCapacity())
                                        .items(instance.getItems())
                                        .build();

                                resultInstances.add(neighbor);
                              }));
        });

    return resultInstances;
  }
}
