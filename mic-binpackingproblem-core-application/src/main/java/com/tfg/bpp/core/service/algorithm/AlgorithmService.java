package com.tfg.bpp.core.service.algorithm;

import com.tfg.bpp.core.model.BppDetailedSolution;
import com.tfg.bpp.core.model.BppInstance;

public interface AlgorithmService {

    BppInstance getSolution(BppInstance bppInstance);

    BppDetailedSolution getDetailedSolution(BppInstance bppInstance);
}
