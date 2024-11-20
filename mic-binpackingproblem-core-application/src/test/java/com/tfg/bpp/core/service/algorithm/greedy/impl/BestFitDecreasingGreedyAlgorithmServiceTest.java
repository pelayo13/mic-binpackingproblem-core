package com.tfg.bpp.core.service.algorithm.greedy.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.tfg.bpp.core.mapper.BppStoredItemMapper;
import com.tfg.bpp.core.model.BppBin;
import com.tfg.bpp.core.model.BppInstance;
import com.tfg.bpp.core.model.BppItem;
import com.tfg.bpp.core.model.BppStoredItem;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BestFitDecreasingGreedyAlgorithmServiceTest {

  @InjectMocks
  private BestFitDecreasingGreedyAlgorithmService bestFitDecreasingGreedyAlgorithmService;

  @Mock private BppStoredItemMapper bppStoredItemMapper;

  @Test
  public void getSolution_OK() {
    BppInstance instance = this.buildInstance();
    BppInstance expectedSolution = this.buildExceptedSolution();

    when(this.bppStoredItemMapper.toBppStoredItem(instance.getItems().get(2), 0))
        .thenReturn(BppStoredItem.builder().size(8).dueDate(1).build());
    when(this.bppStoredItemMapper.toBppStoredItem(instance.getItems().get(0), 1))
        .thenReturn(BppStoredItem.builder().size(3).dueDate(1).build());
    when(this.bppStoredItemMapper.toBppStoredItem(instance.getItems().get(1), 1))
        .thenReturn(BppStoredItem.builder().size(2).dueDate(4).tardiness(0).lateness(-3).build());

    BppInstance solution = this.bestFitDecreasingGreedyAlgorithmService.getSolution(instance);

    assertThat(solution)
        .usingRecursiveComparison()
        .ignoringFields("details")
        .isEqualTo(expectedSolution);
  }

  private BppInstance buildExceptedSolution() {
    return BppInstance.builder()
        .bins(
            List.of(
                BppBin.builder()
                    .items(
                        List.of(
                            BppStoredItem.builder()
                                .size(8)
                                .dueDate(1)
                                .lateness(0)
                                .tardiness(0)
                                .build(),
                            BppStoredItem.builder()
                                .size(2)
                                .dueDate(4)
                                .lateness(-3)
                                .tardiness(0)
                                .build()))
                    .firstUseInstant(1)
                    .maximumLateness(0)
                    .occupiedCapacity(10)
                    .build(),
                BppBin.builder()
                    .items(
                        List.of(
                            BppStoredItem.builder()
                                .size(3)
                                .dueDate(1)
                                .lateness(1)
                                .tardiness(1)
                                .build()))
                    .maximumLateness(1)
                    .occupiedCapacity(3)
                    .firstUseInstant(2)
                    .build()))
        .items(List.of())
        .binsCapacity(10)
        .build();
  }

  private BppInstance buildInstance() {
    return BppInstance.builder().items(this.buildItems()).binsCapacity(10).bins(List.of()).build();
  }

  private List<BppItem> buildItems() {
    return List.of(
        BppItem.builder().size(3).dueDate(1).build(),
        BppItem.builder().size(2).dueDate(4).build(),
        BppItem.builder().size(8).dueDate(1).build());
  }
}
