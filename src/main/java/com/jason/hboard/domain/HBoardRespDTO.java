package com.jason.hboard.domain;

import lombok.*;

import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class HBoardRespDTO {
  private int boardNo;
  private String title;
  private String content;
  private String writer;
  private Timestamp postDate;
  private int readCount;
  private int ref;
  private int step;
  private int refOrder;
}
