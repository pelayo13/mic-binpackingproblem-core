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

  public void setTimeMetrics(int time) {
    this.setLatenessByTime(time);
    this.setTardinessByTime(time);
  }

  private void setLatenessByTime(int time) {
    this.setLateness(time - Optional.ofNullable(this.getDueDate()).orElse(0));
  }

  private void setTardinessByTime(int time) {
    this.setTardiness(Math.max(0, time - Optional.ofNullable(this.getDueDate()).orElse(0)));
  }
}
