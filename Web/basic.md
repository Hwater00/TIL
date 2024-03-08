<hr> 
이벤트 설정

<hr>
문서 객체를 조작할 떼는 DomCountentLoaded 이벤트를 사용
document.addEventListener('DOMcontentLoaded', () => {
})

- CSS 선택자
태그 선택자 : 태그형태로 특정 태그를 가진 요소를 추출합니다.
아이디 선택자: #아이디로 특정 id 속성을 가진 요소를 추출합니다.
클래스 선택자: .클래스로 특정 class 속성을 가진 요소를 출력합니다.
속성 선택자: [속성 = 값] 특정 속성 값을 갖고 있는 요소를 출력합니다.
후손 선택자: 선택자_A_ 선택자_B 선택자_A아래에 있는 선택자_B흫 선택합니다.


head요소와 body 요소 내부에 만든 요소 접근
document.qyerySelector(선택자)
document.querySelectorAll(선택자) 

문서 객체의 부모는 언제나 하나여야 한다. 
appendChild() 메소드 등으로 부모 객체와 이미 연결이 완료된 문서 객체인 경우 parentNode 속성으로 부모 객체에 접근할 수 있음
예시로 문서 객체. parentNode.removeChild(문서 객체)

<hr> 
이벤트 활용
