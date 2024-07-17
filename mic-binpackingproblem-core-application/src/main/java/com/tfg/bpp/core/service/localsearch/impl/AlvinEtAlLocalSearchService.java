package com.tfg.bpp.core.service.localsearch.impl;

import com.tfg.bpp.core.model.BppBin;
import com.tfg.bpp.core.model.BppDetailedSolution;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.service.algorithm.impl.BestFitDecreasingService;
import com.tfg.bpp.core.service.localsearch.LocalSearchService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlvinEtAlLocalSearchService implements LocalSearchService {

  private final BestFitDecreasingService bestFitDecreasingService;

  @Override
  public BppInstance getSolution(BppInstance bppInstance) {
    bppInstance.getBins().sort(Comparator.comparingInt(BppBin::getOccupiedCapacity));
    boolean improved = true;

    while (improved) {
      int previousSolution = bppInstance.getBins().size();
      improved = false;
      if (!bppInstance.getBins().isEmpty() && bppInstance.getBins().size() > 1) {
        bppInstance.getItems().addAll(bppInstance.getBins().get(0).getItems());
        bppInstance.getBins().remove(0);
        this.bestFitDecreasingService.getSolution(bppInstance);
        if (previousSolution > bppInstance.getBins().size()) {
          improved = true;
        }
      }
    }

    return bppInstance;
  }

  @Override
  public BppDetailedSolution getDetailedSolution(BppInstance bppInstance) {
    bppInstance.getBins().sort(Comparator.comparingInt(BppBin::getOccupiedCapacity));
    boolean improved = true;
    List<BppInstance> recordInstances = new ArrayList<>();

    while (improved) {
      int previousSolution = bppInstance.getBins().size();
      improved = false;
      if (!bppInstance.getBins().isEmpty() && bppInstance.getBins().size() > 1) {
        bppInstance.getItems().addAll(bppInstance.getBins().get(0).getItems());
        bppInstance.getBins().remove(0);

        recordInstances.add(bppInstance);
        recordInstances.addAll(
            this.bestFitDecreasingService.getDetailedSolution(bppInstance).getRecordInstances());

        if (previousSolution > bppInstance.getBins().size()) {
          improved = true;
        }
      }
    }

    return BppDetailedSolution.builder().recordInstances(recordInstances).build();
  }
}
