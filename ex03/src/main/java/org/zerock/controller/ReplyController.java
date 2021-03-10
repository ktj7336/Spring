package org.zerock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.service.ReplyService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

// REST방식으로 URL을 설계할 때 PK를 기준으로 작성하는 것이 좋다.
// PK만으로 조회, 수정, 삭제가 가능하기 때문이다.
@RequestMapping("/replies/")
@RestController
@Log4j
@AllArgsConstructor
public class ReplyController {
	
	private ReplyService service;
	
	// REST 방식으로 처리할 때 주의해야 하는 점은 브라우저나 외부에서 서버를 호출할 때 데이터의 포맷과 서버에서 보내주는 데이터 타입을 명확히 설계해야 한다.
	// create()는 @PostMapping으로 POST 방식으로만 작동한다.
	// consumes와 produces를 이용해서 JSON 방식의 데이터만 처리하도록 하고, 문자열을 반환하도록 설계한다.
	// create()의 파라미터는 @RequestBody를 적용해서 JSON 데이터를 ReplyVO 타입으로 변환하도록 지정한다.
	// create()는 내부적으로 ReplyServiceImpl을 호출해서 register()를 호출하고, 댓글이 추가된 숫자를 확인해서 브라우저에 200OK 혹은 500 Internal Server Error를 반환하도록 한다.
	// 테스트시에는 POST방식으로 전송하고, Contet-Type은 application/json으로 지정한다. 
	@PostMapping(value = "/new", consumes = "application/json", produces= {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> create(@RequestBody ReplyVO vo){
		
		log.info("ReplyVO : " + vo);
		
		int insertCount = service.register(vo);
		
		log.info("Reply INSERT COUNT: " + insertCount);
		
		return insertCount == 1
			   ? new ResponseEntity<>("success", HttpStatus.OK)
			   : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		// 삼항연산자
		
	}
	
	// getList()는 Criteria를 이용해서 파라미터를 수집하는데 /{bno}/{page}의 page값은 Criteria를 생성해서 직접 처리해야한다.
	// 게시물의 번호는 @PathVariable을 이용해서 파라미터로 처리하고 브라우저에서 테스트를 해본다.
	@GetMapping(value = "/pages/{bno}/{page}",
				produces = {MediaType.APPLICATION_XML_VALUE,
							MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<ReplyPageDTO> getList(@PathVariable("page") int page, @PathVariable("bno") Long bno){
		
		Criteria cri = new Criteria(page, 10);
		log.info("get Reply List bno:" + bno);
		log.info("cri:" + cri);
		
		return new ResponseEntity<>(service.getListPage(cri, bno), HttpStatus.OK);
	}
	
	// 수정/삭제/조회는 위와 유사한 방식으로 JSON이나 문자열을 반환하도록 한다.
	@GetMapping(value = "/{rno}",
				produces = {MediaType.APPLICATION_XML_VALUE,
							MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<ReplyVO> get(@PathVariable("rno") Long rno){
		
		log.info("get: " + rno);
		
		return new ResponseEntity<>(service.get(rno), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{rno}", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> remove(@PathVariable("rno") Long rno){
		
		log.info("remove: " + rno);
		
		return service.remove(rno) == 1
				? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH},
					value = "/{rno}",
					consumes = "application/json",
					produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> modify(@RequestBody ReplyVO vo, @PathVariable("rno") Long rno){
		
		vo.setRno(rno);
		
		log.info("rno: " + rno);
		log.info("modify:" + vo);
		
		return service.modify(vo) == 1
				? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
