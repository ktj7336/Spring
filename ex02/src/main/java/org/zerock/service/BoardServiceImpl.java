package org.zerock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

// BoardServiceImpl에서 가장 중요한 부분은 @Service 어노테이션이다.
// @Serivce는 계층 구조상 주로 비즈니스 영역을 담당하는 객체임을 표시하기 위해 사용한다.
// @AllArgsContstructor는 모든 파라미터를 이용하는 생성자를 만들어준다.
// 스프링 4.3부터는 단일 파라미터를 받는 생성자의 경우에는 필요한 파라미터를 자동으로 주입할 수 있다.
@Log4j
@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService{
	
	//Spring 4.3이상에서 자동 처리
	private BoardMapper mapper;
	
	
	// BoardService는 void 타입으로 설계되었으므로 mapper.insertSelectKey()의 반환 값인 int를 사용하지 않고 있지만,
	// 필요하다면 예외 처리나 void 대신에 int 타입을 이용해서 사용할 수도 있다.
	@Override
	public void register(BoardVO board) {
		
		log.info("register......" + board);
		
		mapper.insertSelectKey(board);
		
	}
	
	/*
	 * Lombok방식??
	 * @Log4j
	 * @Service
	 * public class BoardServiceImpl implement BoardService{
	 * 
	 * 	@Setter(onMethod_ = @Autowired)
	 *  private BoardMapper mapper;
	 * }	
	 */

	@Override
	public BoardVO get(Long bno) {
		
		log.info("get......" + bno);

		return mapper.read(bno);
	}

	// 수정과 삭제는 정상적으로 이루어지면 1이라는 값이 반환되기 때문에 == 연산자를 이용해서 true/false를 처리할 수 있다.
	@Override
	public boolean modify(BoardVO board) {
		
		log.info("modify....." + board);
		
		return mapper.update(board) == 1;
	}

	@Override
	public boolean remove(Long bno) {
		
		log.info("remove......" + bno);
		
		return mapper.delete(bno) == 1;
	}


	/*
	 * @Override public List<BoardVO> getList() {
	 * 
	 * log.info("getList.........");
	 * 
	 * return mapper.getList(); }
	 */

	@Override
	public List<BoardVO> getList(Criteria cri) {
		
		log.info("get List with criteria: " + cri);
		return mapper.getListWithPaging(cri);
	}

	@Override
	public int getTotal(Criteria cri) {
		
		log.info("get total count");
		return mapper.getTotalCount(cri);
	}
	



}
