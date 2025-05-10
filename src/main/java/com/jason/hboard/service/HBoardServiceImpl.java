package com.jason.hboard.service;

import com.jason.hboard.domain.HBoardResponseDTO;
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
  public List<HBoardResponseDTO> getAllPosts() {
    return hBoardMapper.selectAllHBoard();
  }
}
