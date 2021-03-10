package org.zerock.sample;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

// 생성자 지동주입과 lombok을 결합한 코드
// 만일 여러 개의 인스턴스 변수들 중에서 특정한 변수에 대해서만 생성자를 자성하고 싶다면 @NonNull과 @RequiredArgsConstructor 어노테이션을 이용할 수 있다.
@Component
@ToString
@Getter
@AllArgsConstructor
public class SampleHotel {
	
	//private Chef chef;
	
	@NonNull
	private Chef chef;
		
}
