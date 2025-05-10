package com.jason.hboard.domain;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class HBoardRequestDTO {
  private int boardNo;

  private String title;
  private String content;
  private String writer;
  // private int ref;
  // private int step;
  // private int refOrder;
}
