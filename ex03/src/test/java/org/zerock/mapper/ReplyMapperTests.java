package org.zerock.mapper;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ReplyMapperTests {
	
	// 테스트 전에 해당 번호의 게시물이 존재하는지 확인
	private Long[] bnoArr = {1L, 2L, 53L, 54L, 5L};
	
	@Setter(onMethod_ = @Autowired)
	private ReplyMapper mapper;
	
	// bnoArr은 게시물 번호의 일부로 실제 데이터베이스에 있는 번호여야한다.
	@Test
	public void testCreate() {
	
		IntStream.rangeClosed(1, 10).forEach(i -> {ReplyVO vo = new ReplyVO();
		
		// 게시물 번호
		vo.setBno(bnoArr[i % 5]);
		vo.setReply("댓글 테스트" + i);
		vo.setReplyer("replyer" + i);
		
		mapper.insert(vo);
		});
	}
	
	@Test
	public void testMapper() {
		
		log.info(mapper);
	}
	
	@Test
	public void testRead() {
		
		Long targetRno = 5L;
		
		ReplyVO vo = mapper.read(targetRno);
		
		log.info(vo);
	}
	
	@Test
	public void testDelete() {
		
		Long targetRno = 1L;
		
		mapper.delete(targetRno);
	}
	
	@Test
	public void testUpdate() {
		
		Long targetRno = 10L;
		
		ReplyVO vo = mapper.read(targetRno);
		
		vo.setReply("Update Reply ");
		
		int count = mapper.update(vo);
		
		log.info("UPDATE COUNT: " + count);
	}
	
	@Test
	public void testList() {
		
		Criteria cri = new Criteria();
		
		List<ReplyVO> replies = mapper.getListWithPaging(cri, bnoArr[0]);
		
		replies.forEach(reply -> log.info(reply));
	}
	
	@Test
	public void testList2() {
		
		Criteria cri = new Criteria(2, 10);
		
		List<ReplyVO> replies = mapper.getListWithPaging(cri, 1L);
		
		replies.forEach(reply -> log.info(reply));
	}

}
