package com.tfg.bpp.core.model;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BppStoredItem extends BppItem {

  private Integer lateness;

  private Integer tardiness;

  public BppStoredItem(BppStoredItem item) {
    super(item.getSize(), item.getDueDate());
    this.lateness = item.getLateness();
    this.tardiness = item.getTardiness();
  }

  public void setTimeMetrics(int binInstant) {
    this.setLatenessByTime(binInstant);
    this.setTardinessByTime(binInstant);
  }

  private void setLatenessByTime(int binInstant) {
    this.setLateness(binInstant - Optional.ofNullable(this.getDueDate()).orElse(0));
  }

  private void setTardinessByTime(int binInstant) {
    this.setTardiness(Math.max(0, binInstant - Optional.ofNullable(this.getDueDate()).orElse(0)));
  }
}
