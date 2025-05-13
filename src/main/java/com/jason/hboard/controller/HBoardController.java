package com.jason.hboard.controller;

import com.jason.hboard.domain.HBoardReqDTO;
import com.jason.hboard.domain.HBoardRespDTO;
import com.jason.hboard.domain.PageHBoardReqDTO;
import com.jason.hboard.domain.PageHBoardRespDTO;
import com.jason.hboard.service.HBoardService;
import com.jason.hboard.util.GetClientIpAddr;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


  // ================== 목록(list) 불러오기
  @GetMapping("/hboard/list")
  public String list(PageHBoardReqDTO pageReqDTO, Model model) {

    log.info("◆list.html로 이동...");

    // 파라미터 없이 요청시, 기본값인 pageNo=1, pageSize=15로 DB에 요청됨
    PageHBoardRespDTO<HBoardRespDTO> pageRespDTO = hBoardService.getPostsByPage(pageReqDTO);

    model.addAttribute("pageRespDTO", pageRespDTO);

    return "/hboard/list";
  }

  // ================== 게시글(detail) 불러오기
  @GetMapping("/hboard/detail")
  public String detail(@RequestParam(value = "boardNo", required = false, defaultValue = "-1") int boardNo,
                       PageHBoardReqDTO pageHBoardReqDTO, Model model, HttpServletRequest req) {

    // 클라이언트의 IP주소를 얻어와서 서비스단에 전달
    String ipAddr = GetClientIpAddr.getClientIp(req);

    // 조회수 로직을 거친 후 게시글 불러오기
    HBoardRespDTO hBoardRespDTO = hBoardService.getPostByBoardNoWithIp(boardNo, ipAddr);

    model.addAttribute("hBoardRespDTO", hBoardRespDTO);

    // model.addAttribute("pageHBoardReqDTO", pageHBoardReqDTO);

    /* [[ 생략해도 되는 이유? ]]
    컨트롤러의 파라미터로 PageHBoardReqDTO pageHBoardReqDTO와 같이
    커맨드(또는 폼) 객체를 선언하면, 스프링은 이 객체를 자동으로 생성하고,
    요청 파라미터를 바인딩해주고, 자동으로 모델에 등록
     */

    return "/hboard/detail";
  }



  /* register를 통해, "GET -> 뷰(Form) -> POST" 흐름을 잘 파악하자!
  1. GET요청 : 컨트롤러에서 hBoardRequestDTO 객체(빈 객체)를 모델에 담아 뷰로 전달
  2. 폼 렌더링시: 전달받은 객체의 필드에 폼의 입력필드 값이 연결됨
    - 자바 객체의 필드와 폼의 입력필드가 연결되기 위해서는  입력필드 태그에 th:field="*{title}" 를 입력함으로써 연결됨
    - id="title", name="title", value="hBoardRequestDTO 객체의 title의 값(빈 객체이므로 null)"
  3. 폼 제출(POST) : 입력필드에 입력한 값이 객체에 자동 바인딩(자바객체에 채워짐)되어 컨트롤러에 전달됨
   */

  // ================== 게시글 등록하기
  @GetMapping("/hboard/register")
  public String register(Model model) {

    // 빈 객체 생성 후 뷰로 객체를 전달
    model.addAttribute("hBoardReqDTO", new HBoardReqDTO());

    return "/hboard/register";
  }

  @PostMapping("/hboard/register")
  public String register(@Valid @ModelAttribute("hBoardReqDTO") HBoardReqDTO hBoardReqDTO, BindingResult bindingResult) {
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
    hBoardService.registerPost(hBoardReqDTO);

    // hBoardRequestDTO에 boardNo를 set되었으므로, get하여 GET요청하도록 redirect함
    return "redirect:/hboard/detail?boardNo=" + hBoardReqDTO.getBoardNo();
  }

  // ================== 답글 등록하기
  @GetMapping("/hboard/registerReply")
  public String registerReply(@RequestParam(value = "ref") int ref,
                              @RequestParam(value = "step") int step,
                              @RequestParam(value = "refOrder") int refOrder,
                              PageHBoardReqDTO pageHBoardReqDTO, Model model) {
    // 빈 객체 생성 후
    HBoardReqDTO hBoardReqDTO = new HBoardReqDTO();

    // 뷰단에서 파라미터로 받아온 원래의 게시글의 ref, step, refOrder를 set한 후
    hBoardReqDTO.setRef(ref);
    hBoardReqDTO.setStep(step);
    hBoardReqDTO.setRefOrder(refOrder);

    // 뷰로 객체를 전달
    model.addAttribute("hBoardReqDTO", hBoardReqDTO);

    /*
     Spring MVC가 /templates/hboard/registerReply.html를 찾고,
     템플릿엔진(타임리프)이 최종 HTML을 생성하여 브라우저에 전송함
     */
    return "/hboard/registerReply";
  }

  @PostMapping("/hboard/registerReply")
  public String registerReply(@Valid @ModelAttribute("HBoardReqDTO") HBoardReqDTO hBoardReqDTO, BindingResult bindingResult,
                              PageHBoardReqDTO pageHBoardReqDTO, Model model) {

    if(bindingResult.hasErrors()) {
      return "/hboard/registerReply";
    }

    // 글 등록
    hBoardService.registerReply(hBoardReqDTO);

    return "redirect:/hboard/detail?boardNo=" + hBoardReqDTO.getBoardNo() + "&" + pageHBoardReqDTO.getParams();
  }


  @GetMapping("/hboard/modify")
  public String modify(@RequestParam(value = "boardNo") int boardNo,
                       PageHBoardReqDTO pageHBoardReqDTO, Model model) {
    log.info("pageHBoardReqDTO=" + pageHBoardReqDTO);
    HBoardRespDTO hBoardRespDTO = hBoardService.getPostByBoardForModify(boardNo);

    model.addAttribute("hBoardRespDTO", hBoardRespDTO);
    // 빈 객체 생성 후 뷰로 객체를 전달
    model.addAttribute("hBoardReqDTO", new HBoardReqDTO());
    return "/hboard/modify";
  }

  @PostMapping("/hboard/modify")
  public String modify(@Valid @ModelAttribute("hBoardReqDTO") HBoardReqDTO hBoardReqDTO, BindingResult bindingResult,
                       PageHBoardReqDTO pageHBoardReqDTO, Model model) {

    log.info("pageHBoardReqDTO=" + pageHBoardReqDTO);
    // hBoardReqDTO.setBoardNo(hBoardReqDTO.getBoardNo());
    log.info("::::asfdjlkajdflkj:::::{}", hBoardReqDTO.getBoardNo());

    if(bindingResult.hasErrors()) {
      return "/hboard/modify";
    }

    hBoardService.modifyPost(hBoardReqDTO);
    log.info(":::::::::::::::::::::::::::::{}", hBoardReqDTO.getBoardNo());
    return "redirect:/hboard/detail?boardNo=" + hBoardReqDTO.getBoardNo()+ "&" + pageHBoardReqDTO.getParams();
  }

  @PostMapping("/hboard/remove")
  public String remove(@RequestParam("boardNo") int boardNo, PageHBoardReqDTO pageHBoardReqDTO, Model model) {
    log.info("pageHBoardReqDTO=" + pageHBoardReqDTO);
    hBoardService.removePost(boardNo);

    return "redirect:/hboard/list";
  }

}
