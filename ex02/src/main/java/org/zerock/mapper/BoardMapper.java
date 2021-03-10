package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

// BoardMapper 인터페이스를 작성할 때는 이미 작성된 BoardVO 클래스를 적극적으로 활용해서 필요한 SQL을 어노테이션 속성 값으로 처리할 수 있다.
// ** SQL을 작성할 때는 반드시 ';'이 없도록 작성해야함

public interface BoardMapper {
	
	//@Select("select * from tbl_board where bno > 0")
	public List<BoardVO> getList();
	
	// paging 처리
	public List<BoardVO> getListWithPaging(Criteria cri);
	
	public void insert(BoardVO board);
	
	//public void insertSelectKey(BoardVO board);
	
	public Integer insertSelectKey(BoardVO board);
	
	// read(select)처리 
	public BoardVO read(Long bno);
	
	// delete 처리
	public int delete(Long bno);
	
	// update 처리
	public int update(BoardVO board);
	
	public int getTotalCount(Criteria cri);
}
