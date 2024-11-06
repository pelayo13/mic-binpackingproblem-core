package com.tfg.bpp.core.service.operation;

public interface NeighborhoodStructureOperationServiceFactory {

    public <T> NeighborhoodStructureOperationService<T> getService(String serviceName);
}
