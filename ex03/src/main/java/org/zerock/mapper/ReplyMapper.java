package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

public interface ReplyMapper {
	
	public int insert(ReplyVO vo);
	
	public ReplyVO read(Long bno);
	
	public int delete (Long rno);
	
	public int update(ReplyVO reply);
	
	// 댓글의 목록과 페이징 처리는 기존의 게시물 페이징 처리와 유사하지만, 추가적으로 특정한 게시물의 댓글들만 대상으로 하기 때문에 추가로 게시물의 번호가 필요하게 된다.
	// MyBatis는 두 개 이상이 데이터를 파라미터로 전달하기 위해서는
	// 1. 별도의 객체로 구성한다.
	// 2. Map을 이용한다.
	// 3. @Param을 이용한다.
	// @Param의 속성값은 MyBatis에서 SQL을 이용할때 #{}의 이름으로 사용이 가능하다.
	public List<ReplyVO> getListWithPaging(
			@Param("cri") Criteria cri,
			@Param("bno") Long bno);
	
	public int getCountByBno(Long bno);

}
