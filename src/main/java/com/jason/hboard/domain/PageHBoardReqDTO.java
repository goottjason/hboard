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

  private String params;

  @Valid
  private List<HBoardReqDTO> hBoardReqDTOS;

  public int getOffset() {
    // ex. 3페이지의 offset (3 - 1) x 15 = 30
    return (pageNo - 1) * pageSize;
  }

  public String getParams() {
    if (params == null) {
      params = generateParams();
    }
    return params;
  }

  private String generateParams() {

    /*
    String은 불변 객체라서 문자열을 합치거나 수정할 때마다 새로운 객체가 생성
    StringBuilder는 한 번 객체를 만들면 내부 버퍼에서 문자열을 계속 수정
      - append(str) : 문자열을 끝에 추가
      - toString() : StringBuilder 내용을 String으로 변환
     */

    StringBuilder params = new StringBuilder();
    params.append("pageNo=").append(pageNo).append("&");
    params.append("pageSize=").append(pageSize).append("&");
    return params.toString();
  }


}
