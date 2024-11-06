package com.tfg.bpp.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BppGreedyAlgorithmType {

    RANDOM(ServiceName.RANDOM),

    FIRST_FIT_DECREASING(ServiceName.FIRST_FIT_DECREASING),

    BEST_FIT_DECREASING(ServiceName.BEST_FIT_DECREASING);

    private final String serviceName;

    public interface ServiceName {

        String RANDOM = "RandomAlgorithmService";
        String FIRST_FIT_DECREASING = "FirstFitDecreasingAlgorithmService";
        String BEST_FIT_DECREASING = "BestFitDecreasingAlgorithmService";
    }
}
