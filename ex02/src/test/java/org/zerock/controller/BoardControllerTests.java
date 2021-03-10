package org.zerock.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

// 선언부에서 @WebAppConfiguration 어노테이션을 적용하는 이유는 Servlet의 ServletContext를 이용하기 위해서이다.
// 스프링에서는 WebApplicationContext라는 존재를 이용하기 위해서이다.
@RunWith(SpringJUnit4ClassRunner.class)
// Test for Controller
@WebAppConfiguration
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/spring/root-context.xml",
	"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
// Java Config
// @ContextConfiguration(classes = {
//				org.zerock.RootConfig.class,
//				org.zerock.ServletConfig.class})
@Log4j
public class BoardControllerTests {
	
	@Setter(onMethod_ = {@Autowired})
	private WebApplicationContext ctx;
	
	// MockMvc는 말 그대로 가짜mvc 라고 생각하면 된다
	// 가짜로 URL과 파라미터 등 브라우제어서 사용하는 것처럼 만들어서 Controller를 실행해볼 수 있다.
	private MockMvc mockMvc;
	
	// @Before 어노테이션이 적용된 setUp()에서는 import할 때 JUnit을 이용해야 한다.
	// @Before가 적용된 메서드는 모든 테스트 전에 매번 실행되는 메서드가 된다.
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	// testList()는 MockMvcRequestBuilders라는 존재를 이용해서 GET 방식의 호출을 한다.
	// 이후 에는 BoardController의 getList()에서 반환된 결과를 이용해서 Model에 어떤 데이터들이 담겨 있는지 확인한다.
	@Test
	public void testList() throws Exception{
		
		log.info(
				mockMvc.perform(MockMvcRequestBuilders.get("/board/list"))
				.andReturn()
				.getModelAndView()
				.getModelMap());
	}
	
	// 테스트할 때 MockMvcRequestBuilder의 post()를 이용하면 POST 방식으로 데이터를 전달할 수 있고,
	// param()을 이용해서 전달해야 하는 파라미터들을 지정할 수 있다.
	// <input> 태그와 유사한 역활이다...
	@Test
	public void testRegister() throws Exception{
		
		String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/register")
				.param("title","테스트 새글 제목")
				.param("content", "테스트 새글 내용")
				.param("writer", "user00")
				).andReturn().getModelAndView().getViewName();
		
		log.info(resultPage);
	
	}
	
	// 특정 게시물을 조회할 때 반드시 bno라는 파라미터가 필요하므로 param()을 통해서 추가하고 실행한다.
	@Test
	public void tetGet() throws Exception{
		
		log.info(mockMvc.perform(MockMvcRequestBuilders.get("/board/get")
				.param("bno", "2"))
				.andReturn()
				.getModelAndView().getModelMap());
		
	}
	
	@Test
	public void testModify() throws Exception{
		
		String reusltPage = mockMvc
				.perform(MockMvcRequestBuilders.post("/board/modify")
						.param("bno", "1")
						.param("title", "update new test title")
						.param("content", "update new test content")
						.param("writer", "user01"))
				.andReturn().getModelAndView().getViewName();
		
		log.info(reusltPage);
	}
	
	// MockMvc를 이용해서 파라미터를 전달할 때에는 문자열로만 처리해야 한다.
	@Test
	public void testRemove() throws Exception{
		
		// 삭제전 데이터베이스에 게시물 번호를 확인할 것
		String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/remove")
				.param("bno", "25")
				).andReturn().getModelAndView().getViewName();
		
		log.info(resultPage);
	}
	
	@Test
	public void testListPaging() throws Exception{
		
		log.info(mockMvc.perform(
				MockMvcRequestBuilders.get("/board/list")
				.param("pageNum", "2")
				.param("amount", "50"))
				.andReturn().getModelAndView().getModelMap());
	}
	

}
