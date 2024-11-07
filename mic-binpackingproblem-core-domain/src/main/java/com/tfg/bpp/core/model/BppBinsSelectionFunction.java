package com.tfg.bpp.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BppBinsSelectionFunction {
  SMALLEST_OCCUPIED_CAPACITY() {
    @Override
    public List<Integer> getSelection(List<BppBin> bins, int numberBinsToSelect) {
      List<Integer> sizesOfSelection = new ArrayList<>();
      List<Integer> indexesOfSelection = new ArrayList<>();
      IntStream.range(0, bins.size())
          .forEach(
              index -> {
                if (index < numberBinsToSelect) {
                  indexesOfSelection.add(index);
                  sizesOfSelection.add(bins.get(index).getOccupiedCapacity());
                } else {
                  OptionalInt indexOfMaximumSize =
                      IntStream.range(0, sizesOfSelection.size())
                          .reduce(
                              (index1, index2) ->
                                  sizesOfSelection.get(index1) > sizesOfSelection.get(index2)
                                      ? index1
                                      : index2);
                  if (indexOfMaximumSize.isPresent()
                      && sizesOfSelection.get(indexOfMaximumSize.getAsInt())
                          > bins.get(index).getOccupiedCapacity()) {
                    sizesOfSelection.set(
                        indexOfMaximumSize.getAsInt(), bins.get(index).getOccupiedCapacity());
                    indexesOfSelection.set(indexOfMaximumSize.getAsInt(), index);
                  }
                }
              });

      return indexesOfSelection;
    }
  },
  BIGGEST_MAXIMUM_LATENESS() {
    @Override
    public List<Integer> getSelection(List<BppBin> bins, int numberBinsToSelect) {
      List<Integer> maximumLatenessOfSelection = new ArrayList<>();
      List<Integer> indexesOfSelection = new ArrayList<>();
      IntStream.range(0, bins.size())
          .forEach(
              index -> {
                if (index < numberBinsToSelect) {
                  indexesOfSelection.add(index);
                  maximumLatenessOfSelection.add(bins.get(index).getMaximumLateness());
                } else {
                  OptionalInt indexOfSmallestMaximumLateness =
                      IntStream.range(0, maximumLatenessOfSelection.size())
                          .reduce(
                              (index1, index2) ->
                                  maximumLatenessOfSelection.get(index1)
                                          < maximumLatenessOfSelection.get(index2)
                                      ? index1
                                      : index2);
                  if (indexOfSmallestMaximumLateness.isPresent()
                      && maximumLatenessOfSelection.get(indexOfSmallestMaximumLateness.getAsInt())
                          < bins.get(index).getMaximumLateness()) {
                    maximumLatenessOfSelection.set(
                        indexOfSmallestMaximumLateness.getAsInt(),
                        bins.get(index).getMaximumLateness());
                    indexesOfSelection.set(indexOfSmallestMaximumLateness.getAsInt(), index);
                  }
                }
              });

      return indexesOfSelection;
    }
  };

  public abstract List<Integer> getSelection(List<BppBin> bins, int numberBinsToSelect);
}
