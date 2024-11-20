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
public class BppInstanceMetrics {

  private int numberItems;

  private List<BppAlgorithmMetrics> algorithmsMetrics;
}
