package com.tfg.bpp.core.service.localsearch;

import java.util.function.Supplier;

import com.tfg.bpp.core.service.localsearch.impl.AlvinEtAlLocalSearchService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LocalSearchServiceFactory {

    ALVIN_ET_AL(AlvinEtAlLocalSearchService.class);

    private final Class<? extends LocalSearchService> localSearchServiceClass;
}
