package com.tfg.bpp.core.service.operation;

import com.tfg.bpp.core.model.BppInstance;

import java.util.List;

public interface NeighborhoodStructureOperationService<T> {

  List<BppInstance> apply(List<BppInstance> instances, T operation);
}
