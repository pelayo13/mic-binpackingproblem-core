package com.tfg.bpp.core.config;

import com.tfg.bpp.core.service.algorithm.greedy.GreedyAlgorithmServiceFactory;
import com.tfg.bpp.core.service.operation.NeighborhoodStructureOperationServiceFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoryConfig {

    @Bean
    public FactoryBean<?> algorithmServiceFactoryBean() {
        final ServiceLocatorFactoryBean bean = new ServiceLocatorFactoryBean();
        bean.setServiceLocatorInterface(GreedyAlgorithmServiceFactory.class);
        return bean;
    }

    @Bean
    public FactoryBean<?> neighborhoodStructureOperationServiceFactoryBean() {
        final ServiceLocatorFactoryBean bean = new ServiceLocatorFactoryBean();
        bean.setServiceLocatorInterface(NeighborhoodStructureOperationServiceFactory.class);
        return bean;
    }
}
