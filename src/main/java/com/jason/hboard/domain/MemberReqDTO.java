package com.jason.hboard.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class MemberReqDTO {
  private String memberId;
  private String memberPwd;
  private String memberName;
  private String memberMobile;
  private String memberEmail;

  @Builder.Default // 기본값 적용하려면 추가
  private String memberImg = "avatar.png";
  @Builder.Default
  private String memberGender = "U";
  @Builder.Default
  private int memberPoint = 1000;
}
