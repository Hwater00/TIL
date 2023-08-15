JAR과 WAR은 JAVA의 java -jar을 이용해 생성된 압축(아카이브) 파일로 애플리케이션 배포, 동작 관현 파일을 패키징한 것<br>

1. JAR은 JAVA Archice의 약자
- JAVA 어플리케이션이 동작할 수 있도록 자바 프로젝트를 압축한 파일
- Class (JAVA리소스, 속성 파일), 라이브러리 파일을 포함함
- JRE(JAVA Runtime Environment)만 있어도 실행 가능함 (java -jar 프로젝트네임.jar)


<br>
2. WAR은 Aeb Application Archive의 약자
- Servlet / Jsp 컨테이너에 배치할 수 있는 웹 애플리케이션(Web Application) 압축파일 포맷
- 웹 관련 자원을 포함함 (JSP, Servlet, JAR, Class, XML, HTML, Javascript)
-  사전 정의된 구조를 사용함 (WEB-INF, META-INF)
-  별도의 웹서버(WEB) or 웹 컨테이너(WAS) 필요
-  JAR파일의 일종으로 웹 애플리케이션 전체를 패키징 하기 위한 JAR 파일이다.

=> Sprinag boot에서 가이드하는 표준은 JAR,  JSP를 사용하여 화면 구성/ 외장 WAS를 이용 시 WAR
