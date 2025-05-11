package com.jason.hboard.controller;

import com.jason.hboard.domain.HBoardRequestDTO;
import com.jason.hboard.domain.HBoardResponseDTO;
import com.jason.hboard.domain.PageHBoardRequestDTO;
import com.jason.hboard.domain.PageHBoardResponseDTO;
import com.jason.hboard.mapper.HBoardMapper;
import com.jason.hboard.service.HBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  public String list(PageHBoardRequestDTO pageHBoardRequestDTO, Model model) {

    log.info("◆list.html로 이동...");

    // List<HBoardResponseDTO> hBoardResponseDTO = hBoardService.getAllPosts();

    PageHBoardResponseDTO<HBoardResponseDTO> hBoardResponseDTO = hBoardService.getPostsByPage(pageHBoardRequestDTO);

    model.addAttribute("pageDto", hBoardResponseDTO);
    return "/hboard/list";
  }
  @GetMapping("/hboard/detail")
  public String detail(Model model, @RequestParam(value = "boardNo") int boardNo) {
    HBoardResponseDTO hBoardResponseDTO = hBoardService.getPostByBoardNo(boardNo);
    model.addAttribute("hBoardResponseDTO", hBoardResponseDTO);
    return "/hboard/detail";
  }



  /* register를 통해, "GET -> 뷰(Form) -> POST" 흐름을 잘 파악하자!
  1. GET요청 : 컨트롤러에서 hBoardRequestDTO 객체(빈 객체)를 모델에 담아 뷰로 전달
  2. 폼 렌더링시: 전달받은 객체의 필드에 폼의 입력필드 값이 연결됨
    - 자바 객체의 필드와 폼의 입력필드가 연결되기 위해서는  입력필드 태그에 th:field="*{title}" 를 입력함으로써 연결됨
    - id="title", name="title", value="hBoardRequestDTO 객체의 title의 값(빈 객체이므로 null)"
  3. 폼 제출(POST) : 입력필드에 입력한 값이 객체에 자동 바인딩(자바객체에 채워짐)되어 컨트롤러에 전달됨
   */

  @GetMapping("/hboard/register")
  public String register(Model model) {
    // 빈 객체 생성 후
    HBoardRequestDTO hBoardRequestDTO = new HBoardRequestDTO();
    // 뷰로 객체를 전달
    model.addAttribute("hBoardRequestDTO", hBoardRequestDTO);

    return "/hboard/register";
  }

  @PostMapping("/hboard/register")
  public String register(@Valid @ModelAttribute("hBoardRequestDTO") HBoardRequestDTO hBoardRequestDTO,
                         BindingResult bindingResult) {
    /*
    @Valid : 각 필드를 유효성 검사하여 실패한 필드와 메시지가 BindingResult 객체에 저장됨
    @ModelAttribute : 뷰에서 컨트롤러로 입력한 데이터가 자바 객체의 각 필드에 바인딩되어 전달됨
    BindingResult : @ModelAttribute 바로 뒤에 붙여야하고, 에러가 있으면 정보를 수집
     */

    // 에러가 있으면, 뷰를 생성하여 전송
    if(bindingResult.hasErrors()) {
      /*
      Spring MVC가 /templates/hboard/register.html를 찾고,
      템플릿엔진(타임리프)이 데이터(입력값, 에러메시지 등)을 채워넣고, 최종 HTML을 생성하여 브라우저에 전송함
       */
      return "/hboard/register";
    }

    // 글 등록
    hBoardService.registerPost(hBoardRequestDTO);

    // hBoardRequestDTO에 boardNo를 set되었으므로, get하여 GET요청하도록 redirect함
    return "redirect:/hboard/detail?boardNo=" + hBoardRequestDTO.getBoardNo();
  }

  @GetMapping("/hboard/registerReply")
  public String registerReply(@RequestParam("ref") int ref,
                              @RequestParam("step") int step,
                              @RequestParam("refOrder") int refOrder,
                              Model model) {
    // 빈 객체 생성 후
    HBoardRequestDTO hBoardRequestDTO = new HBoardRequestDTO();

    // 뷰단에서 파라미터로 받아온 원래의 게시글의 ref, step, refOrder를 set한 후
    hBoardRequestDTO.setRef(ref);
    hBoardRequestDTO.setStep(step);
    hBoardRequestDTO.setRefOrder(refOrder);

    // 뷰로 객체를 전달
    model.addAttribute("hBoardRequestDTO", hBoardRequestDTO);

    /*
     Spring MVC가 /templates/hboard/registerReply.html를 찾고,
     템플릿엔진(타임리프)이 최종 HTML을 생성하여 브라우저에 전송함
     */
    return "/hboard/registerReply";
  }

  @PostMapping("/hboard/registerReply")
  public String registerReply(@Valid @ModelAttribute("hBoardRequestDTO") HBoardRequestDTO hBoardRequestDTO,
                              BindingResult bindingResult) {
    if(bindingResult.hasErrors()) {
      return "/hboard/registerReply";
    }

    // 글 등록
    hBoardService.registerReply(hBoardRequestDTO);

    return "redirect:/hboard/list";
  }

}
