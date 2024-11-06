package com.tfg.bpp.core.service.algorithm;

import com.tfg.bpp.core.model.BppGreedyAlgorithmType;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppLocalSearchAlgorithm;
import com.tfg.bpp.core.model.BppLocalSearchSolution;

public interface AlgorithmService {

  BppInstance getSolution(BppInstance instance, BppGreedyAlgorithmType greedyAlgorithmType);

  BppLocalSearchSolution getLocalSearchSolution(
      BppInstance instance, BppLocalSearchAlgorithm localSearchAlgorithm);
}
