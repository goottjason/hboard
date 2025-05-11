package com.jason.hboard.service;

import com.jason.hboard.domain.HBoardRequestDTO;
import com.jason.hboard.domain.HBoardResponseDTO;
import com.jason.hboard.domain.PageHBoardRequestDTO;
import com.jason.hboard.domain.PageHBoardResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface HBoardService {
  PageHBoardResponseDTO<HBoardResponseDTO> getPostsByPage(PageHBoardRequestDTO pageHBoardRequestDTO);
  PageHBoardResponseDTO<HBoardResponseDTO>  getPostByBoardNo(PageHBoardRequestDTO pageHBoardRequestDTO);

  void registerPost(@Valid HBoardRequestDTO hBoardRequestDTO);

  void registerReply(@Valid HBoardRequestDTO hBoardRequestDTO);
}
