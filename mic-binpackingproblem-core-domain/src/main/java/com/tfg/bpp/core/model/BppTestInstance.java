package com.tfg.bpp.core.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BppTestInstance {

  private int binsCapacity;

  private int minItemsSize;

  private int maxItemsSize;

  private int minItemsDueDate;

  private int maxItemsDueDate;

  private List<Integer> numberItems;

  private int numberInstances;

  private List<BppAlgorithm> algorithms;
}
