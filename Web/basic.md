## 이벤트 기초
문서 객체를 조작할 떼는 DomCountentLoaded 이벤트를 사용
document.addEventListener('DOMcontentLoaded', () => { // (이벤트 이름, 콜백 함수)
})
이 코드는 document라는 문서 객체의 DOMContentLoaded 이벤트가 발생했을 때, 매개변수로 지정한 콜백 함수를 실행해라


- CSS 선택자
태그 선택자 : 태그형태로 특정 태그를 가진 요소를 추출합니다.
아이디 선택자: #아이디로 특정 id 속성을 가진 요소를 추출합니다.
클래스 선택자: .클래스로 특정 class 속성을 가진 요소를 출력합니다.
속성 선택자: [속성 = 값] 특정 속성 값을 갖고 있는 요소를 출력합니다.
후손 선택자: 선택자_A_ 선택자_B 선택자_A아래에 있는 선택자_B흫 선택합니다.


- head요소와 body 요소 내부에 만든 요소 접근
document.qyerySelector(선택자)
document.querySelectorAll(선택자) 

문서 객체의 부모는 언제나 하나여야 한다. 
appendChild() 메소드 등으로 부모 객체와 이미 연결이 완료된 문서 객체인 경우 parentNode 속성으로 부모 객체에 접근할 수 있음
예시로 문서 객체. parentNode.removeChild(문서 객체)


## 이벤트 활용
표준 이벤크 모델& 고전 이벤트 모델 & 인라인 이벤트 모델

- 이벤트 리스너 외부 분리
1) event.currentTarget()
2) this 키워드


- 입력 내부 양식 글자 수
focus 이벤트와 blur 이벤트 사용.
입력 양식에 초첨을 맞춘 경우(활성화 상태)와 초첨을 해제한 경우(비활성화 상태)에 발생하는 이벤트
입력 양식에 글자를 입력하려고 선택한 순간부터 타이머를 돌리고, 다른 일을 하기 위해서 입력 양식에서 초점을 해제하면 타이머를 정지하게 만든 것입니다.
이를 통해 어떤 상황에서도, 어떤 언어를 입력해도 글자 수를 정상적으로 출력
'' textarea.addEventListener('focus',(event)=>{ // 입력 양식 활성화  
timerId = serInterval(()=>{ 
  const length = textarea.value.length
  h1.textContent = `글자 수: ${length}`,50)
})

textarea.addEventListenr('blue',(event)v=>{ // 입력 양식 비활성화
  clearInterval(timerId)
})


## localStorage 객체 
- localStorage 객체는 웹 브라우저가 기본적으로 제공하는 객체입니다. 
localStorage.getItem(키) : 저장되 값을 추출합니다. 없으면 undefined가 나옵니다. 객체의 속성을 추출하는 일반적인 형태로 localStorage.키 또는 localStorage[키] 령태로 ㅅ용 할 수도 있습니다.
localStoreage.setItem(키,값): 값을 저장합니다. 이전과 마찬가지로 객체에 솏성을 지정하는 일반적인 형태를 사용할 수도 있습니다.
localStoreage.removeItem(키): 특정 키의 값을 제거합니다.
localStorage.clear():저장된 모든 값을 제거합니다.

- 웹 브라우저가 제공하는 기능을 웹 API라고 부릅니다.
  배터리 정보 추출 API, 블루투스 API, 2D/3D를 처리를 빠르게 하는 캔버스 API, 위치 정보 API, 드래그앤드롭 API, 이미지 동영상 캡쳐 API, 네트워크 정보 API, 웹 오디오 미디 처리 API 등이 있습니다.

