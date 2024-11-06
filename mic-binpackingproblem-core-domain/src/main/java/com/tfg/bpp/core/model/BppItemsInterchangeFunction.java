package com.tfg.bpp.core.model;

import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BppItemsInterchangeFunction {
    SIZE() {
        @Override
        public boolean isImproved(List<? extends BppItem> itemsFrom, List<? extends BppItem> itemsTo) {
            int sizeOfFrom = itemsFrom.stream().mapToInt(BppItem::getSize).sum();
            int sizeOfTo = itemsTo.stream().mapToInt(BppItem::getSize).sum();

            return sizeOfTo < sizeOfFrom;
        }
    },
    ALWAYS() {
        @Override
        public boolean isImproved(List<? extends BppItem> itemsFrom, List<? extends BppItem> itemsTo) {
            return true;
        }
    };

    public abstract boolean isImproved(
            List<? extends BppItem> itemsFrom, List<? extends BppItem> itemsTo);
}
