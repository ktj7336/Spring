package org.zerock.sample;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

// @ContextConfuguration은 지정된 클래스나 문자열을 이용해서 필요한 객체들을 스프링 내에 객체로 등록하게 된다.
// @Autowired는 해당 인스턴스 변수가 스프링으로부터 자동으로 주입해 달라는 표시이다.
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class SampleTests {
	
	@Setter(onMethod_ = { @Autowired} )
	private Restaurant restaurant;
	
	@Test
	public void textExist() {
		
		assertNotNull(restaurant);
		
		log.info(restaurant);
		log.info("---------------------------------");
		log.info(restaurant.getChef());
	}

}

// 실행된 결과에서 주목해야 하는 부분
// new Restaurant()와 같이 Restaurant 클래스에서 객체를 생성한 적이 없는데도 객체가 만들어졌다는 점
// 스프링은 관리가 필요한 객체(Bean)를 어노테이션 등을 이용해서 객체를 생성하고 관리하는 일종의 '컨테이너'나 '팩토리'기능을 가지고 있다.
// Restaurant 클래스의 @Data 어노테이션으로 Lombok을 이용해서 여러 메서드가 만들어진점 
// Lombok은 자동으로 getter/sette 등을 만들어 주는데 스프링은 생성자 주입 혹은 setter 주입을 이용해서 동작합니다.
// Lombok을 통해서 getter/setter 등을 자동으로 생성하고 'onMethod'속성을 이요해서 작성된 setter에 @Autowired 어노테이션을 추가한다
// Restaurant 객체의 Chef 인스턴스 변수(멤버변수)에 Chef 타입의 객체가 주입되어 있다는점 
// 스프링은 @Autowired와 같은 어노테이션을 이용해서 개발자가 직접 객체들과의 관계를 관리하지 않고 자동으로 관리되도록 한다.
