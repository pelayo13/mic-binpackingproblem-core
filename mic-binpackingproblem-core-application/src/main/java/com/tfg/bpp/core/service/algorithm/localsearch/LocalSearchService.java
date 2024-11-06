package com.tfg.bpp.core.service.algorithm.localsearch;

import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppLocalSearchAlgorithm;
import com.tfg.bpp.core.model.BppLocalSearchSolution;
import com.tfg.bpp.core.model.BppSolution;

public interface LocalSearchService {

    BppLocalSearchSolution getSolution(BppInstance bppInstance, BppLocalSearchAlgorithm localSearchAlgorithm);
}
