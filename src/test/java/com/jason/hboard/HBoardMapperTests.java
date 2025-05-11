package com.jason.hboard;


import com.jason.hboard.domain.HBoardRequestDTO;
import com.jason.hboard.domain.MemberRequestDTO;
import com.jason.hboard.mapper.HBoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@SpringBootTest
@Slf4j
// @RequiredArgsConstructor
@Transactional
public class HBoardMapperTests {
  @Autowired
  private DataSource dataSource;
  @Autowired
  private HBoardMapper hBoardMapper;

  @Test
  @Rollback(value = false)
  public void testInsertNewMember() {
    MemberRequestDTO memberRequestDTO = MemberRequestDTO.builder()
      .memberId("hong1234")
      .memberPwd("asdf")
      .memberName("Hong Gil Dong")
      .memberMobile("010-1234-5678")
      .memberEmail("hong1234@goott5.com")
      // .memberImg()
      // .memberGender()
      .build();
    log.info("◆result : {}", hBoardMapper.insertNewMember(memberRequestDTO));
  }

  @Test
  @Rollback(value = false)
  public void testSelectMember() {
    String memberId = "hong1234";
    log.info("◆result : {}", hBoardMapper.selectMember(memberId));
  }

  @Test
  @Rollback(value = false)
  public void testRegisterPost() {
    // 1. 글 등록
    HBoardRequestDTO hBoardRequestDTO = HBoardRequestDTO.builder()
      .title("첫번째 글")
      .content(null)
      .writer("hong1234")
      .build();
    log.info("◆result : {}", hBoardMapper.insertNewPost(hBoardRequestDTO));

    // 2. 생성된 boardNo로 ref를 수정
    log.info("◆result : {}", hBoardMapper.updateSetRefToBoardNo(hBoardRequestDTO.getBoardNo()));

  }

  @Test
  @Rollback(value = false)
  public void testSelectAllHBoard() {

    log.info("◆result : {}", hBoardMapper.selectAllPosts());

  }

  @Test
  @Rollback(value =false)
  public void testRegisterDummyData() {
    for (int i = 0; i < 500; i++) {
      HBoardRequestDTO hBoardRequestDTO = HBoardRequestDTO.builder()
        .title("dummy 제목 " + i)
        .content("dummy 내용 " + i)
        .writer("hong1234")
        .build();
      hBoardMapper.insertNewPost(hBoardRequestDTO);
      hBoardMapper.updateSetRefToBoardNo(hBoardRequestDTO.getBoardNo());
    }
  }
}
