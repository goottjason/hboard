package com.jason.hboard.service;

import com.jason.hboard.domain.HBoardRequestDTO;
import com.jason.hboard.domain.HBoardResponseDTO;
import com.jason.hboard.domain.PageHBoardRequestDTO;
import com.jason.hboard.domain.PageHBoardResponseDTO;
import com.jason.hboard.mapper.HBoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HBoardServiceImpl implements HBoardService {
  private final HBoardMapper hBoardMapper;

  @Override
  public PageHBoardResponseDTO<HBoardResponseDTO> getPostsByPage(PageHBoardRequestDTO pageHBoardRequestDTO) {
    // 현재 총 게시글의 수를 카운트
    int totalPosts = hBoardMapper.selectTotalPostsCount();

    // pageNo를 토대로 만들어진 offset과 pageSize로 posts를 리스트에 담음
    List<HBoardResponseDTO> dtoList = hBoardMapper.selectPostsByPage(pageHBoardRequestDTO);

    /* 요청할 당시의 pageHBoardRequestDTO의 값이 필요한 이유?
    blockEndPage, blockStartPage, lastPage, showPrevBlockButton, showNextBlockButton의 값을 응답할 때
    pageNo와 pageSize를 토대로 식이 구성되므로 필요함
     */

    // 커스텀 빌더메서드
    return PageHBoardResponseDTO.<HBoardResponseDTO>withPageInfo()
      .pageHBoardRequestDTO(pageHBoardRequestDTO)
      .responseDTOList(dtoList)
      .totalPosts(totalPosts)
      .build();
  }

  @Override
  public PageHBoardResponseDTO<HBoardResponseDTO> getPostByBoardNo(PageHBoardRequestDTO pageHBoardRequestDTO) {

    int totalPosts = hBoardMapper.selectTotalPostsCount();

    List<HBoardResponseDTO> dtoList = hBoardMapper.selectPostByboardNo(pageHBoardRequestDTO);

    // 커스텀 빌더메서드
    return PageHBoardResponseDTO.<HBoardResponseDTO>withPageInfo()
      .pageHBoardRequestDTO(pageHBoardRequestDTO)
      .responseDTOList(dtoList)
      .totalPosts(totalPosts)
      .build();
  }

  @Override
  public void registerPost(HBoardRequestDTO hBoardRequestDTO) {
    // 1. 게시글 저장
    hBoardMapper.insertNewPost(hBoardRequestDTO);
    hBoardMapper.updateSetRefToBoardNo(hBoardRequestDTO.getBoardNo());
  }

  @Override
  public void registerReply(HBoardRequestDTO hBoardRequestDTO) {
    // 1. insert할 답글보다 refOrder가 큰 답글들에 대해서 (과거글은 아래에 보여지므로) refOrder를 하나씩 더하여 뒤로 미룸
    hBoardMapper.updateSetRefOrderPlusOne(hBoardRequestDTO.getRef(), hBoardRequestDTO.getRefOrder());

    // 2. 등록할 답글의 step은 원글의 +1, refOrder도 원글의 +1 (바로 아래 계단(step)에, 바로 밑(refOrder)에 보여져야 하므로)
    hBoardRequestDTO.setStep(hBoardRequestDTO.getStep() + 1);
    hBoardRequestDTO.setRefOrder(hBoardRequestDTO.getRefOrder() + 1);

    log.info("◆insertNewReply : {}",hBoardMapper.insertNewReply(hBoardRequestDTO));

    /*if (hBoardMapper.insertNewReply(hBoardRequestDTO) == 1) {
    }*/
  }
}
