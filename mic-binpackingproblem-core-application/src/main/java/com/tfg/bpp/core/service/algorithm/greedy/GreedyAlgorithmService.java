package com.tfg.bpp.core.service.algorithm.greedy;

import com.tfg.bpp.core.model.BppDetailedSolution;
import com.tfg.bpp.core.model.BppInstance;

public interface GreedyAlgorithmService {

    BppInstance getSolution(BppInstance bppInstance);

    BppDetailedSolution getDetailedSolution(BppInstance bppInstance);
}
