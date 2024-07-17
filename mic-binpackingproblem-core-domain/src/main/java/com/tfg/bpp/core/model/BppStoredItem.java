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

  private Integer tardiness;

  public void setTardinessByTime(int time) {
    this.setTardiness(time - Optional.ofNullable(this.getDueDate()).orElse(0));
  }
}
