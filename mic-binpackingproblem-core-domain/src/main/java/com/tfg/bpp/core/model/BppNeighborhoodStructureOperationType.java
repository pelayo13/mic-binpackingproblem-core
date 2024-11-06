package com.tfg.bpp.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BppNeighborhoodStructureOperationType {

    BINS_DESTRUCTION(ServiceName.BINS_DESTRUCTION),

    RECONSTRUCTION(ServiceName.RECONSTRUCTION),

    BINS_INTERCHANGE(ServiceName.BINS_INTERCHANGE),

    STORED_ITEMS_AND_FREE_ITEMS_INTERCHANGE(ServiceName.STORED_ITEMS_AND_FREE_ITEMS_INTERCHANGE),

    STORED_ITEMS_INTERCHANGE(ServiceName.STORED_ITEMS_INTERCHANGE);

    private final String serviceName;

    public interface ServiceName {

        String BINS_DESTRUCTION = "BinsDestructionOperationService";
        String RECONSTRUCTION = "ReconstructionOperationService";
        String BINS_INTERCHANGE = "BinsInterchangeOperationService";
        String STORED_ITEMS_AND_FREE_ITEMS_INTERCHANGE = "StoredItemsAndFreeItemsInterchangeOperationService";
        String STORED_ITEMS_INTERCHANGE = "StoredItemsInterchangeOperationService";
    }
}
