package org.zerock.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.SampleDTO;
import org.zerock.domain.SampleDTOList;
import org.zerock.domain.TodoDTO;

import lombok.extern.log4j.Log4j;


// Log4j 빨간줄 해결 <scope>runtime</scope> 이부분을 주석 처리해준다.
@Controller
@RequestMapping("/sample/*")
@Log4j
public class SampleController {
	
	// TodoDTO에는 특별하게 dueDate 변수의 타입이 java.util.Date 타입이다.
	// 만일 사용자가'2021-01-01'과 같이 들어오는 데이터를 변환하고자 할 때 문제가 발생하게 된다.
	// 이러한 문제의 간단한 해결책은 @InitBinder를 이용하는 것이다.s
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	@RequestMapping("")
	public void basic() {
		
		log.info("basic..................");
	}
	
	// 스프링 4.3버전 부터 @RequsetMapping을 줄여서 사용할 수 있는 @GetMapping, @PostMapping이 등장하는데
	// 축약형의 표현이므로,아래와 같이 비교해면서 학습을 해보는것이 좋다.
	@GetMapping("/basicOnlyGet")
	public void basicGet2() {
		
		log.info("basic get only get.................");
	}
	
	@GetMapping("/ex01")
	public String ex01(SampleDTO dto) {
		
		log.info("" + dto);
		
		return "ex01";
	}
	
	// @RequestParam은 파라미터로 사용된 변수의 이름과 전달괴는 파라미터의 이름이 다른 경우에 유용하게 사용된다.
	@GetMapping("/ex02")
	public String ex02(@RequestParam("name") String name, @RequestParam("age") int age) {
		
		log.info("name : " + name);
		log.info("age : " + age);
		
		return "ex02";
	}
	// 동일한 이름의 파라미터가 여러 개 전달되는 경우에는 ArrayList<> 등을 이용해서 처리가 가능하다.
	// 스프링은 파라미터의 타입을 보고 객체를 생성하므로 파라미터의 타입은 List<>와 같이 인터페이스 타입이 아닌 실제적인 클래스 타입으로 지정.
	// 코드의 경우 'ids'라는 이름의 파라미터가 여러 개 전달되라도 ArrayList<String>이 생성되어 자동으로 수집이 된다.
	// 배열도 동일하게 처리가 가능하다.
	@GetMapping("/ex02List")
	public String ex02List(@RequestParam("ids")ArrayList<String> ids) {
		
		log.info("ids: " + ids);
		
		return "ex02List";
	}
	
	@GetMapping("/ex02Array")
	public String ex02Array(@RequestParam("ids") String[] ids) {
		
		log.info("ids: " + Arrays.toString(ids));
		
		return "ex02Array";
	}
	
	@GetMapping("/ex02Bean")
	public String ex02Bean(SampleDTOList list) {
		
		log.info("list dtos: " + list);
		
		return "ex02Bean";
	}
	
	// 브라우저 http://localhost:8005/sample/ex03?title=test&dueData=2020-01-01 과 같이 호출했다면 서버에서는 정상적으로 파라미터를 수집해서 처리해야 한다.
	// 만약 @InitBinder 처리가 되지 않는다면 브라우저에 400에러가 발생하는 것을 볼수 있다. (400에러는 요청 구문(syntax)이 잘못되었다는 뜻)
	@GetMapping("/ex03")
	public String ex03(TodoDTO todo) {
		
		log.info("todo: " + todo);
		
		return "ex03";
	}
	
	// ex04()는 SampleDTO 타입과 int 타입의 데이터를 파라미터로 사용한다.
	// 결과를 확인할려면 /WEB-INF/views 폴더 아래 sample 폴더를 생성하고 리턴값에서 사용한 ex04에 해당하는 ex04.jsp를 생성한다.
	/*
	 * @GetMapping("/ex04") public String ex04(SampleDTO dto, int page) {
	 * 
	 * log.info("dto: " + dto); log.info("page: " + page);
	 * 
	 * return "/sample/ex04"; }
	 */
	// @ModelAttribute를 사용할 경우
	@GetMapping("/ex04")
	public String ex04(SampleDTO dto, @ModelAttribute("page") int page) {
	
		log.info("dto: " + dto);
		log.info("page: " + page);
		
		return "/sample/ex04";
	}
	
	// 주소를 치고 들어가면 404에러가 뜨는데 그 이유는 jsp파일이 존재하지 않아서이다.
	@GetMapping("/ex05")
	public void ex05() {
		
		log.info("/ex05..........");
	}
	
	@GetMapping("/ex06")
	public @ResponseBody SampleDTO ex06() {
		
		log.info("/ex06...........");
		
		SampleDTO dto = new SampleDTO();
		dto.setAge(10);
		dto.setName("홍길동");
		
		return dto;
	}
	// ResponseEntity는 HttpHaders 객체를 같이 전달할 수 있고, 이를 통해서 원하는 Http 헤더 메세지를 가공하는 것이 가능
	// ex07()의 경우 브라우저에는 JSON타입이라는 헤더 메세지와 200 OK 라는 상태코드를 전송한다.
	@GetMapping("/ex07")
	public ResponseEntity<String> ex07() {
		
		log.info("/ex07..........");
		
		// {"name" : 홍길동}
		String msg = "{\"name\" : \"홍길동\"}";
		
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json;charset=UTF-8");
		
		return new ResponseEntity<>(msg, header, HttpStatus.OK);
	}
	
	@GetMapping("/exUpload")
	public void exUpload() {
		
		log.info("/exUpload.......");
	}
	
	@PostMapping("/exUploadPost")
	public void exUploadPost(ArrayList<MultipartFile> files) {
		
		files.forEach(file -> {
			log.info("----------------------------------");
			log.info("name:" + file.getOriginalFilename());
			log.info("size:" + file.getSize());
		});
	}

}
