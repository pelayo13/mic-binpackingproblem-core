package com.tfg.bpp.core.mapper;

import com.tfg.bpp.core.config.CentralMapperConfig;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppItem;
import com.tfg.bpp.core.model.BppLocalSearchSolution;
import com.tfg.bpp.core.model.BppStoredItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface BppInstanceMapper {

    BppInstance toBppInstance(BppLocalSearchSolution solution);

    BppLocalSearchSolution toBppLocalSearchSolution(BppInstance solution);
}
