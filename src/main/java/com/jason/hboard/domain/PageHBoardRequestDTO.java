package com.jason.hboard.domain;

import lombok.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class PageHBoardRequestDTO {
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

  private String params;

  public int getOffset() {
    return (pageNo - 1) * pageSize;
    // ex. 3페이지의 offset (3 - 1) x 15 = 30
  }
  public String getParams() {
    return "pageNo=" + pageNo + "&pageSize=" + pageSize;
  }
}
