package org.zerock.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Setter;

// Chef 객체를 필요로 하는 클래스
// @Component는 스프링에게 해당 클래스가 스프링에서 관리해야 하는 대상임을 표시하는 어노테이션이다
// @Setter는 자동으로 setChef()를 컴파일 시 생성한다
// @Setter에서 사용된 onMethod 속성은 생성되는 setChef()에 @Autowired 어노테이션을 추가하도록한다
@Component
@Data
public class Restaurant {
	
	@Setter(onMethod_ = @Autowired)
	private Chef chef;

}
