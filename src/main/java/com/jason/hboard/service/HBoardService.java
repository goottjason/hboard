package com.jason.hboard.service;

import com.jason.hboard.domain.HBoardResponseDTO;

import java.util.List;

public interface HBoardService {
  List<HBoardResponseDTO> getAllPosts();
}
