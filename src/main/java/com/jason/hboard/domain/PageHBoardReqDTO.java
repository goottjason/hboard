package com.jason.hboard.domain;

import jakarta.validation.Valid;
import lombok.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class PageHBoardReqDTO {

  @Builder.Default
  @Min(value = 1)
  @Positive
  private int pageNo = 1;

  @Builder.Default
  @Min(value = 10)
  @Max(value = 100)
  @Positive
  private int pageSize = 15;

  private int offset;

  private int boardNo;
  private int ref;
  private int step;
  private int refOrder;

  @Valid
  private List<HBoardReqDTO> hBoardReqDTOS;

  public int getOffset() {
    // ex. 3페이지의 offset (3 - 1) x 15 = 30
    return (pageNo - 1) * pageSize;
  }

}
