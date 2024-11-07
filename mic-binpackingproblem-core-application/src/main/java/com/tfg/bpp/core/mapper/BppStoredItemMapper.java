package com.tfg.bpp.core.mapper;

import com.tfg.bpp.core.config.CentralMapperConfig;
import com.tfg.bpp.core.model.BppItem;
import com.tfg.bpp.core.model.BppStoredItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface BppStoredItemMapper {

    @Mapping(target = "tardiness", ignore = true)
    BppStoredItem toBppStoredItemWithoutTardiness(BppItem item);

    BppItem toBppItem( BppStoredItem item);

    default BppStoredItem toBppStoredItem(BppItem item, Integer binInstant) {
        BppStoredItem storedItem = this.toBppStoredItemWithoutTardiness(item);
        storedItem.setTimeMetrics(binInstant);

        return storedItem;
    }
}
