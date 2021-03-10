package org.zerock.domain;

import java.util.Date;

import lombok.Data;

// BoardVO 클래스는 Lombok을 이용해서 생성자와 getter/setter, toString()등을 만들어내는 방식을 사용한다.
// 이를 위해서 @Data 어노테이션을 적용한다.
@Data
public class BoardVO {
	
	private Long bno;
	private String title;
	private String content;
	private String writer;
	private Date regdate;
	private Date updateDate;

}




