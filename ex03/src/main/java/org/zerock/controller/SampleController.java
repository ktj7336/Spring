package org.zerock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.SampleVO;
import org.zerock.domain.Ticket;

import lombok.extern.log4j.Log4j;

// 기존의 @Controller는 문자열을 반환하는 경우에는 JSP 파일의 이름으로만 처리하지만, @RestController의 경우에는 순수한 데이터가 된다.
// @GetMapping에 사용된 produces 속성은 해당 메서드가 생산하는 MIME 타입을 의미한다.
// 예제와 같이 문자열로 직접 지정할 수도 있고, 메서드 내에 MediaType이라는 클래스를 이용할 수도 있다.

@RestController
@RequestMapping("/sample")
@Log4j
public class SampleController {
	
	@GetMapping(value = "/getText", produces = "test/plain; charset=UTF-8")
	public String getText() {
		
		log.info("MIME TYPE: " + MediaType.TEXT_PLAIN_VALUE);
		
		return "안녕하세요";
	}
	
	// getSample은 XML과 JSON 방식의 데이터를 생성할 수 있도록 작성이 되었다.
	@GetMapping(value = "/getSample", 
				produces = {MediaType.APPLICATION_JSON_UTF8_VALUE,
							MediaType.APPLICATION_XML_VALUE})
	public SampleVO getSample() {
		
		return new SampleVO(112, "스타", "로드");
	}
	
	@GetMapping(value = "/getSample2")
	public SampleVO getSample2() {
		
		return new SampleVO(113, "로켓", "라쿤");
	}
	
	// getList()는 내부적으로 1부터 10미만까지의 루프를 처리하면서 SampleVO 객체를 만들어서 List<SampleVO>로 만들어낸다.
	// 브라우저를 통해서 /sample/getList를 호출하면 기본적으로는 XML 데이터를 전송하는 것을 볼 수 있다.
	// 뒤에 .json을 붙혀주면 [] 로 싸여진 JSON 형태의 배열 데이터를 볼 수 있다.
	@GetMapping(value = "/getList")
	public List<SampleVO> getList(){
		
		return IntStream.range(1, 10).mapToObj(i -> new SampleVO(i, i + "First", i + "Last"))
				.collect(Collectors.toList());
	}
	
	// Map을 이용하는 경우에는 키(key)에 속하는 데이터는 XML로 변환되는 경우에 태그의 이름이 되기 때문에 문자열을 지정한다.
	@GetMapping(value = "/getMap")
	public Map<String, SampleVO> getMap(){
		
		Map<String, SampleVO> map = new HashMap<>();
		map.put("First", new SampleVO(111, "그루트", "주니어"));
		
		return map;
	}
	
	// Rest 방식으로 호출하는 경우는 화면 자체가 아니라 데이터 자체를 전송하는 방식으로 처리되기 때문에
	// 데이터를 요청한 쪽에서는 정상적인 데이터인지 비정상적인 데이터인지를 구분할 수 있는 확실한 방법을 제공해야만 한다.
	// ResponseEntity는 데이터와 함께 HTTP 헤더의 상태 메세지 등을 같이 전달하는 용도로 사용한다.
	// HTTP의 상태 코드와 에러 메세지 등을 함께 데이터를 전달할 수 있기 때문에 받는 입장에서는 확실하게 결과를 알수 있다.
	// check()는 반드시 height와 weight를 파라미터로 전달 받는다.
	// 만일 height 값이 150 보다 작으면 502(bad gateway) 상태 코드와 데이터를 전송하고 그렇지 않다면 OK 코드와 데이터를 전송한다.
	@GetMapping(value = "/check", params = {"height", "weight"})
	public ResponseEntity<SampleVO> check(Double height, Double weight){
		
		SampleVO vo = new SampleVO(0, "" + height, "" + weight);
		
		ResponseEntity<SampleVO> result = null;
		
		if(height < 150) {
			result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
		}else {
			result = ResponseEntity.status(HttpStatus.OK).body(vo);
		}
		
		return result;
	}
	
	// @PathVariable을 적용하고 싶은 경우에는 {}를 이용해서 변수명을 지정하고, @PathVariable을 이용해서 지정된 이름의 변숫값을 얻을수 있다.
	// 값을 얻을 때에는 int,double과 같은 기본 자료형은 사용할 수 없다.
	@GetMapping("/product/{cat}/{pid}")
	public String[] getPath(@PathVariable("cat") String cat, @PathVariable("pid") Integer pid ) {
		
		return new String[] { "category: " + cat, "productid: " + pid};
	}
	
	@PostMapping("/ticket")
	public Ticket convert(@RequestBody Ticket ticket) {
		
		log.info("convert...........ticket" + ticket);
		
		return ticket;
	}

}
