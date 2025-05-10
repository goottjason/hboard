package com.jason.hboard.mapper;

import com.jason.hboard.domain.HBoardRequestDTO;
import com.jason.hboard.domain.HBoardResponseDTO;
import com.jason.hboard.domain.MemberRequestDTO;
import com.jason.hboard.domain.MemberResponseDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HBoardMapper {

  @Insert("""
    INSERT INTO selfmember (memberId, memberPwd, memberName, memberMobile, memberEmail, memberImg, memberGender) 
    VALUES (#{memberId}, #{memberPwd}, #{memberName}, #{memberMobile}, #{memberEmail}, #{memberImg}, #{memberGender})""")
  int insertNewMember(MemberRequestDTO member);

  @Select("SELECT * FROM selfmember where memberId = #{memberId}")
  List<MemberResponseDTO> selectMember(@Param("memberId") String memberId);

  @Insert("""
    INSERT INTO selfhboard (title, content, writer)
    VALUES (#{title}, #{content}, #{writer})""")
  @Options(useGeneratedKeys = true, keyProperty = "boardNo")
  int insertNewHBoard(HBoardRequestDTO hBoard);

  @Update("UPDATE selfhboard SET ref = #{boardNo} WHERE boardNo = #{boardNo}")
  int updateSetRefToBoardNo(@Param("boardNo") int boardNo);

  @Select("SELECT * FROM selfhboard")
  List<HBoardResponseDTO> selectAllHBoard();

  @Select("SELECT * FROM selfhboard WHERE boardNo = #{boardNo}")
  HBoardResponseDTO selectPostFromHBoard(@Param("boardNo") int boardNo);
}
