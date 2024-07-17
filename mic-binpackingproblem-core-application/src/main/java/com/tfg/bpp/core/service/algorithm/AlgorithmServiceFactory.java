package com.tfg.bpp.core.service.algorithm;

import com.tfg.bpp.core.service.algorithm.impl.BestFitDecreasingService;
import com.tfg.bpp.core.service.algorithm.impl.FirstFitDecreasingService;
import com.tfg.bpp.core.service.algorithm.impl.RandomAlgorithmService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlgorithmServiceFactory {

    RANDOM(RandomAlgorithmService.class),
    FIRST_FIT_DECREASING(FirstFitDecreasingService.class),
    BEST_FIT_DECREASING(BestFitDecreasingService.class);

    private final Class<? extends AlgorithmService> algorithmServiceClass;
}
