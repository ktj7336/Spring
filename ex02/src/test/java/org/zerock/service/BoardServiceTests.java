package org.zerock.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardServiceTests {
	
	@Setter(onMethod_ = {@Autowired})
	private BoardService service;
	
	@Test
	public void testExist() {
		
		log.info(service);
		assertNotNull(service);
	}
	
	@Test
	public void testRegister() {
		
		BoardVO board = new BoardVO();
		board.setTitle("새로 작성하는 글");
		board.setContent("새로 작성하는 내용");
		board.setWriter("newbie");
		
		service.register(board);
		
		log.info("생성된 게시물의 번호:" + board.getBno());
		
	}
	
	@Test
	public void testGetList() {
		
		//service.getList().forEach(board -> log.info(board));
		service.getList(new Criteria(2, 10)).forEach(board -> log.info(board));
	}
	
	@Test
	public void testGet() {
		
		log.info(service.get(1L));
	}
	
	// testDelete()의 경우에는 해당 게시물이 존재할 때 true를 반환하는 것을 확인할 수 있다.
	@Test
	public void testDelete() {
		
		// 게시글의 번호의 존재 여부를 확인하고 테스트 한다.
		log.info("REMOVE RESULT : " + service.remove(21L));
	}
	
	// testUpdate()의 경우에는 특정한 게시물을 먼저 조회하고, title 값을 수정한 후 업데이트 한다.
	@Test
	public void testUpdate() {
		
		BoardVO board = service.get(1L);
		
		if (board == null) {
			return;
		}
		
		board.setTitle("제목 수정");
		log.info("MODIFY RESULT : " + service.modify(board));
	}
		
	

}
