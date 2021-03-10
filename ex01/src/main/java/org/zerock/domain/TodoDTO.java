package org.zerock.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class TodoDTO {
	
	private String title;
	// @DateTimeFormat를 사용할경우 InitBinder가 필요하지 않는다
	@DateTimeFormat(pattern  = "yyyy/MM/dd")
	private Date dueDate;

}
