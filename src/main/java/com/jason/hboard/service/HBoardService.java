package com.jason.hboard.service;

import com.jason.hboard.domain.HBoardReqDTO;
import com.jason.hboard.domain.HBoardRespDTO;
import com.jason.hboard.domain.PageHBoardReqDTO;
import com.jason.hboard.domain.PageHBoardRespDTO;
import jakarta.validation.Valid;

public interface HBoardService {

  PageHBoardRespDTO<HBoardRespDTO> getPostsByPage(PageHBoardReqDTO pageReqDTO);

  HBoardRespDTO getPostByBoardNoWithIp(int boardNo, String ipAddr);

  void registerPost(@Valid HBoardReqDTO hBoardReqDTO);

  void registerReply(@Valid HBoardReqDTO hBoardReqDTO);

  HBoardRespDTO getPostByBoardForModify(int boardNo);

  void modifyPost(@Valid HBoardReqDTO hBoardReqDTO);
}
