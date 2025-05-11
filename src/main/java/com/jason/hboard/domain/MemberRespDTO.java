package com.jason.hboard.domain;

import lombok.*;

import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class MemberRespDTO {
  private String memberId;
  private String memberName;
  private String memberMobile;
  private String memberEmail;
  private Timestamp memberRegDate;
  private String memberImg;
  private int memberPoint;
  private String memberGender;
}
