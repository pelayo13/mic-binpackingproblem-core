package com.tfg.bpp.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BppInstance {

  private List<BppItem> items;

  private List<BppBin> bins;

  private int binsCapacity;

  private BppDetailsOfSolution details;

  public BppInstance(BppInstance instance) {
    this.items =
        instance.getItems().stream()
            .map(item -> new BppItem(item.getSize(), item.getDueDate()))
            .collect(Collectors.toList());
    this.bins = instance.getBins().stream().map(BppBin::new).collect(Collectors.toList());
    this.binsCapacity = instance.getBinsCapacity();
    this.details =
        Optional.ofNullable(instance.getDetails())
            .map(BppDetailsOfSolution::new)
            .orElse(null);
  }

  public void removeBins(List<Integer> indexOfBins) {
    indexOfBins.forEach(
        index ->
            this.getItems()
                .addAll(
                    this.getBins().get(index).getItems().stream()
                        .map(item -> new BppItem(item.getSize(), item.getDueDate()))
                        .toList()));
    this.getBins().removeAll(indexOfBins.stream().map(index -> this.getBins().get(index)).toList());
    this.recalculateFirstUseInstants();
  }

  public void recalculateFirstUseInstants() {
    IntStream.range(0, this.getBins().size())
        .forEach(index -> this.getBins().get(index).setFirstUseInstant(index));
  }

  public int getTardiness() {
    return this.getBins().stream()
        .flatMapToInt(bppBin -> bppBin.getItems().stream().mapToInt(BppStoredItem::getTardiness))
        .sum();
  }

  public int getLateness() {
    return this.getBins().stream()
        .flatMapToInt(bppBin -> bppBin.getItems().stream().mapToInt(BppStoredItem::getLateness))
        .sum();
  }

  public int getAvailableCapacity() {
    return this.getBins().stream()
        .mapToInt(bin -> bin.getAvailableCapacity(this.getBinsCapacity()))
        .sum();
  }

  public double getAverageTardinessStoredItems() {
    return this.getBins().stream()
        .flatMapToInt(bppBin -> bppBin.getItems().stream().mapToInt(BppStoredItem::getTardiness))
        .average()
        .orElse(0);
  }

  public double getAverageLatenessStoredItems() {
    return this.getBins().stream()
        .flatMapToInt(bppBin -> bppBin.getItems().stream().mapToInt(BppStoredItem::getLateness))
        .average()
        .orElse(0);
  }

  public double getMaximumLateness() {
    return this.getBins().stream().mapToInt(BppBin::getMaximumLateness).max().orElse(0);
  }

  public int getAvailableCapacity(int capacity) {
    return this.getBins().stream().mapToInt(bin -> bin.getAvailableCapacity(capacity)).sum();
  }

  public double getFitness() {
    return 0.5 * this.getBins().size() + 0.5 * this.getMaximumLateness();
  }

  public void recalculateAllMetrics() {
    this.recalculateFirstUseInstants();
    this.getBins().forEach(BppBin::recalculateOccupiedCapacity);
  }
}
