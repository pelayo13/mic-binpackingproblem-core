package com.tfg.bpp.core.model;

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
public class BppBinsDestructionOperation extends BppNeighborhoodStructureOperation{

  private int numberToDestroy;

  private BppBinsSelectionFunction binsSelectionFunction;
}
