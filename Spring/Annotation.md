1)어노테이션(Annotation)이란, 프로그램에 추가 정보를 제공하는 메타데이터(데어터를 위한 데이터)의 역할이다. <br>
자바에서 코드 사이에 주석처럼 사용하여 특별한 의미, 기능을 수행하도록 하는 기술로 클래스와 메서드에 추가되어 다양한 기능을 부여할 수 있다.
<br>

2)스프링의 대표적인 어노테이션
- @Component
  개발자가 생성한 Class를 Spring의 Bean으로 등록할 때 사용하는 어노테이션.
- @Controller
  Spring에게 해당 Class가 Controller의 역할을 한다고 명시하기 위해 사용하는 어노테이션.
- @RequestMapping
  호출하는 클라이언트의 정보를 가져다가 서버(Controller)에게 전달해주는 매핑
  - @RequestMapping(value=" ")형태로 작성
  - 요청 들어온 URI의 요청과 Annotation value 값이 일치하면 해당 클래스나 메소드가 실행
  - 클래스 단위 사용: 하위 메소드에 모두 적용, 메소드 단위 사용: 해당 메소드에서 지정한 방식으로 URL처리
- @Autowired
  Spring Framework에서 Bean 객체를 주입받기 위한 방법= 의존성 주입으로 사용하는 어노테이션.
  @Autowired 어노테이션이 적용된 생성자, 필드, 메소드에 대해 의존 자동 주입을 처리한다.
  - @Qualifier
    동일한 타입을 가진 bean 객체가 있을 때, 사용할 의존 객체를 선택할 수 있도록 해주는 한정자 값을 설정한다.
    
