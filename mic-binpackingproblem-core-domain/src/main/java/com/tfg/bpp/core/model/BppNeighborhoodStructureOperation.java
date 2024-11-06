package com.tfg.bpp.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BppNeighborhoodStructureOperation {

  private BppNeighborhoodStructureOperationType type;
}
