package com.jason.hboard.service;

import com.jason.hboard.domain.HBoardReqDTO;
import com.jason.hboard.domain.HBoardRespDTO;
import com.jason.hboard.domain.PageHBoardReqDTO;
import com.jason.hboard.domain.PageHBoardRespDTO;
import com.jason.hboard.mapper.HBoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HBoardServiceImpl implements HBoardService {
  private final HBoardMapper hBoardMapper;

  @Override
  public PageHBoardRespDTO<HBoardRespDTO> getPostsByPage(PageHBoardReqDTO pageReqDTO) {
    // 현재 총 게시글의 수를 카운트
    int totalPosts = hBoardMapper.selectTotalPostsCount();

    // pageNo를 토대로 만들어진 offset과 pageSize로 posts를 리스트에 담음
    List<HBoardRespDTO> hBoardRespDTOS = hBoardMapper.selectPostsByPage(pageReqDTO);

    /* 요청할 당시의 pageHBoardRequestDTO의 값이 필요한 이유?
    blockEndPage, blockStartPage, lastPage, showPrevBlockButton, showNextBlockButton의 값을 응답할 때
    pageNo와 pageSize를 토대로 식이 구성되므로 필요함
     */

    // 커스텀 빌더메서드
    return PageHBoardRespDTO.<HBoardRespDTO>withPageInfo()
      .pageHBoardRequestDTO(pageReqDTO)
      .respDTOS(hBoardRespDTOS)
      .totalPosts(totalPosts)
      .build();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public HBoardRespDTO getPostByBoardNoWithIp(int boardNo, String ipAddr) {

    // 1. 게시글 불러오기
    HBoardRespDTO hBoardRespDTO= hBoardMapper.selectPostByboardNo(boardNo);

    // 2. selfhboardlog 테이블에서 ipAddr 유저가 boardNo 글의 최근읽은시점의 현재시점과 차이값을 시간으로 구하기
    /*
    조회한 값이 없다면, -1
    조회한 값이 있다면, 그 시간차이를 반환
     */
    int dateDiff = hBoardMapper.selectDateDiff(boardNo, ipAddr);

    if(dateDiff == -1) {
      // 3. 최초 조회면 로그 추가
      hBoardMapper.insertLog(ipAddr, boardNo);
    } else if (dateDiff >= 24) {
      // 3. 최근읽은 시점으로부터 24시간이 넘은 경우, 로그의 readWhen을 현재시점으로 업데이트
      hBoardMapper.updateLog(ipAddr, boardNo);
    }

    // DB에서 조회수 1 증가
    hBoardMapper.incrementReadCount(boardNo);
    // 증가된 조회수를 hBoardRespDTO에 set하기
    hBoardRespDTO.setReadCount(hBoardRespDTO.getReadCount() + 1);

    // 과정을 거친 후, 게시글 반환
    return hBoardMapper.selectPostByboardNo(boardNo);
  }

  @Override
  public void registerPost(HBoardReqDTO hBoardReqDTO) {
    // 1. 게시글 저장
    hBoardMapper.insertNewPost(hBoardReqDTO);
    hBoardMapper.updateSetRefToBoardNo(hBoardReqDTO.getBoardNo());
  }

  @Override
  public void registerReply(HBoardReqDTO hBoardRequestDTO) {
    // 1. insert할 답글보다 refOrder가 큰 답글들에 대해서 (과거글은 아래에 보여지므로) refOrder를 하나씩 더하여 뒤로 미룸
    hBoardMapper.updateSetRefOrderPlusOne(hBoardRequestDTO.getRef(), hBoardRequestDTO.getRefOrder());

    // 2. 등록할 답글의 step은 원글의 +1, refOrder도 원글의 +1 (바로 아래 계단(step)에, 바로 밑(refOrder)에 보여져야 하므로)
    hBoardRequestDTO.setStep(hBoardRequestDTO.getStep() + 1);
    hBoardRequestDTO.setRefOrder(hBoardRequestDTO.getRefOrder() + 1);

    log.info("◆insertNewReply : {}",hBoardMapper.insertNewReply(hBoardRequestDTO));

    /*if (hBoardMapper.insertNewReply(hBoardRequestDTO) == 1) {
    }*/
  }

  @Override
  public HBoardRespDTO getPostByBoardForModify(int boardNo) {
    return hBoardMapper.selectPostByboardNo(boardNo);
  }

  @Override
  public void modifyPost(HBoardReqDTO hBoardReqDTO) {
    hBoardMapper.updatePost(hBoardReqDTO);
  }
}
