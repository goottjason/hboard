package com.jason.hboard.controller;

import com.jason.hboard.domain.HBoardResponseDTO;
import com.jason.hboard.mapper.HBoardMapper;
import com.jason.hboard.service.HBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HBoardController {

  private final HBoardService hBoardService;

  @GetMapping("/")
  public String index() {
    log.info("◆index.html로 이동...");
    return "index";
  }

  @GetMapping("/hboard/list")
  public String list(Model model) {
    log.info("◆list.html로 이동...");
    List<HBoardResponseDTO> hBoardResponseDTO = hBoardService.getAllPosts();

    model.addAttribute("hBoardResponseDTO", hBoardResponseDTO);
    return "/hboard/list";
  }
  @GetMapping("/hboard/detail")
  public String detail(Model model) {

    return "/hboard/detail";
  }

}
