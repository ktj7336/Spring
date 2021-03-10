package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

// BoardController는 @Controller 어노테이션을 추가해서 스프링의 빈으로 인식할 수 있게 하고,
// @RequsetMapping을 통해서 /board로 시작하는 모든 처리를 BoardController가 하도록 지정을 해준다.

// BoardController는 BoardService에 대해서 의존적이므로 @AllArgsConstructor를 이용해서 생성자를 만들고 자동으로 주입하도록 한다.
// 만일 생성자를 만들지 않을 경우 @Setter(onMethod_ = {@Autowired}) 를 이용해서 처리를 한다.
// list()는 나중에 게시물의 목록을 전달해야 하므로 Model을 파라미터로 지정하고, 이를 통해서 BoardServiceImpl 객체의 getList() 결과를 담아 전달한다(addAttribute).
@Controller
@Log4j
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {
	
	private BoardService service;
	
	/*
	 * @GetMapping("/list") public void list(Model model) {
	 * 
	 * log.info("list");
	 * 
	 * model.addAttribute("list", service.getList()); }
	 */
	
	// list()는 pageMaker라는 이름으로 PageDTO 클래스에서 객체를 만들어서 Model에 담아준다.
	// PageDTO를 구성하기 위해서 전체 데이터 수가 필요한데, 아직 그 처리가 이루어 지지 않아서 123을 임의의 값으로 지정을 한다.
	@GetMapping("/list")
	public void list(Criteria cri, Model model) {
		
		log.info("list: " + cri);
		model.addAttribute("list", service.getList(cri));
		//model.addAttribute("pageMaker", new PageDTO(cri, 123));
		
		int total = service.getTotal(cri);
		
		log.info("total: " + total);
		
		model.addAttribute("pageMaker", new PageDTO(cri, total));
	}
	
	// register() 메서드는 조금 다르게 String을 리턴 타입으로 지정하고, RedirectAttributes를 파라미터로 지정한다.
	// 이는 등록 작업이 끝난 후 다시 목록 화면으로 이동하기 위함인데, 추가적으로 새롭게 등록된 게시물의 번호를 같이 전달하기 위해서
	// RedirectAttributes를 이용한다.
	// 리턴 시에는 redirect: 접두어를 사용하는데 이를 이용하면 스프링 MVC가 내부적으로 response.sendRedirect()를 처리해 주기 때문이다.
	@PostMapping("/register")
	public String register(BoardVO board, RedirectAttributes rttr) {
		
		log.info("register : " + board);
		
		service.register(board);
		
		rttr.addFlashAttribute("result", board.getBno());
		
		return "redirect:/board/list";
	}
	
	// BoardController의 get() 메서드에는 bno 값을 좀 더 명시적으로 처리하는 @ReqeustParam을 이용해서 지정한다.
	//(파라미터 이름과 변수 이름을 기준으로 동작하기 때문에 생략해도 무방)
	// 또한 화면 쪽으로 해당 번호의 게시물을 전달해야 하므로 Model을 파라미터로 지정한다.
	@GetMapping({"/get", "/modify"})
	public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, Model model) {
		
		log.info("/get or modify");
		model.addAttribute("board", service.get(bno));
	}
	
	// service.modify()는 수정 여부를 boolean으로 처리하므로 이를 이용해서 성공한 경우에만 RedirectAttributes에 추가한다.
	@PostMapping("/modify")
	public String modify(BoardVO board, @ModelAttribute("cri") Criteria cri ,RedirectAttributes rttr) {
		log.info("modify : " + board);
		
		if(service.modify(board)) {
			rttr.addFlashAttribute("result", "success");
		}
		
		/*
		 * rttr.addAttribute("pageNum", cri.getPageNum()); rttr.addAttribute("amount",
		 * cri.getAmount()); rttr.addAttribute("type", cri.getType());
		 * rttr.addAttribute("keyword", cri.getKeyword());
		 */
		
		return "redirect:/board/list" + cri.getListLink();
	}
	
	// BoardController의 remove()는 삭제 후 페이지의 이동이 필요하므로 RedirectAttribute를 파라미터로 사용하고 
	// redirect를 이용해서 삭제 처리 후에 다시 목록 페이지로 이동한다.
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		
		log.info("remove...." + bno);
		if (service.remove(bno)) {
			rttr.addFlashAttribute("result", "success");
		}
		
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		return "redirect:/board/list";
	}
	
	@GetMapping("/register")
	public void register() {
		
	}
	
	

}
