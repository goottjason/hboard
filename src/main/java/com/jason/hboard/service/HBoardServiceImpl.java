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

  /*@Override
  public List<HBoardResponseDTO> getAllPosts() {
    return hBoardMapper.selectAllHBoard();
  }*/

  @Override
  public PageHBoardResponseDTO<HBoardResponseDTO> getPostsByPage(PageHBoardRequestDTO pageHBoardRequestDTO) {

    List<HBoardResponseDTO> dtoList = hBoardMapper.selectPostsByPage(pageHBoardRequestDTO);

    int totalPosts = hBoardMapper.selectTotalPostsCount();

    return PageHBoardResponseDTO.<HBoardResponseDTO>withPageInfo()
      .pageHBoardRequestDTO(pageHBoardRequestDTO)
      .responseDTOList(dtoList)
      .totalPosts(totalPosts)
      .build();
  }

  @Override
  public HBoardResponseDTO getPostByBoardNo(int boardNo) {
    return hBoardMapper.selectPostByboardNo(boardNo);
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
