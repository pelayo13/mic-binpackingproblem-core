package com.tfg.bpp.core.service.algorithm.greedy;

import com.tfg.bpp.core.service.algorithm.greedy.GreedyAlgorithmService;

public interface GreedyAlgorithmServiceFactory {

    public GreedyAlgorithmService getService(String serviceName);
}
