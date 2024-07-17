package com.tfg.bpp.core.service.localsearch;

import com.tfg.bpp.core.model.BppDetailedSolution;
import com.tfg.bpp.core.model.BppInstance;

public interface LocalSearchService {

    BppInstance getSolution(BppInstance bppInstance);

    BppDetailedSolution getDetailedSolution(BppInstance bppInstance);
}
