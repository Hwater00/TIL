DAO는 Data Access Object의 약자로, DB의 데이터에 접근하기 위한 객체 <br>
DB의 접근하기 위한 로직을 분리하기 위해 사용하는데  DAO에서는 직접 DB에 접근하여 data를 삽입, 삭제,조회 등 CRUD 기능을 조작할 수 있는 기능을 수행한다.<br>
Service와 DB를 연결하는 고리 역할을 한다
=>주로 쿼리문을 작성하는 부분, 실제로 DB의 data에 접근

DTO는 Data Transfer Object의 약자로, 로직을 가지지 않응 순순한 계층 간 데이터 교환을 위한 Java Bean(객체)를 의미한다.
DTO는 로직을 가지지 않는 데이터 객체로 가변의성격을 가진 클래스이며 데이터전송, 교환을 위해 존재 
=>getter, setter 메소드만 가진 클래스
