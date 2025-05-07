# 먼저  querydsl의 필요성은 무엇일까?
가장 크게,런타임시에 쿼리가 문제가 있는지 확인하는 문법 오류를 자바 컴파일러가 잡지 못하는 단점을 해결할 수 있습니다.
    * 런타임 오류 같은 경우, 유저가 실행할 때 오루를 발견한다는 문제
자바 코드 형식으로 메서드로 뽑아낼 수 있는 등 자바 코드로싸의 장점을 활용할 수 있습니다.

# 설정 유의 사항
Generated된 Q파일은 git에 올리지 않기를 권장한다.
-> 빌드 폴더 내부에 들어감으로 깃에 올라가지 않는다.

# 스프링 부트 3.x(2.6 이상), Querydsl 5.0 지원
-  Querydsl은 향후 fetchCount()fetchResult() 를 지원하지 않기로 결정했습니다.
- org.springframework.data.support.PageableExecutionUtils 패키지 변경

# JPAQueryFactory
쿼리와 관련된 건 다 Q타입을 넣어야 한다.
Test 코드의 트랜잭션이 달리면 기본적으로 롤백이 진행된다. 데이터가 유지되길 바란다면 @Commit을 넣어 DB 데이터를 확인할 수 있다. 단)자종화된 테스트는 메모리에서 실행되어야 함으로 실제 DB에 남은면 안 된다.
JPAQueryFactory를 필드로 제공하면 동시성 문제는 어떻게 될까? 동시성 문제는 JPAQueryFactory를 생성
할 때 제공하는 EntityManager(em)에 달려있다. 스프링 프레임워크는 여러 쓰레드에서 동시에 같은
EntityManager에 접근해도, 트랜잭션 마다 별도의 영속성 컨텍스트를 제공하기 때문에, 동시성 문제는 걱정하
지 않아도 된다.

# 라이브러리 살펴보기
querydsl-qpt는 Q제작용, querydsl-jpa 애플리케이션 제작 시 사용
HikariCp 데이터 베이스 커넥션 풀링

#기본 Q-Type 활용
Q클래스 인스턴스를 사용하는 2가지 방법
1) QMember qMember = new QMember("m"); 별칭 직접 지정
2) QMember qMember = QMember.member; 기본 인스턴스 사용
3) 권장방법:  import static study.querydsl.entity.QMember.*; -> QMember에 이미 선언된 public static final QMember member = new QMember("member1"); 불러와서 사용하기


# 문법
1. select , from을 selectFrom으로 합칠 수 있음
2. 검색조건 and는 체인 외에도 ,로 넘길 수 있음
3. JPQL이 제공하는 모든 검색 조건 제공
```java 
    member.username.eq("member1") // username = 'member1'
     member.username.ne("member1") //username != 'member1'
     member.username.eq("member1").not() // username != 'member1'
     member.username.isNotNull() //이름이 is not null
     member.age.in(10, 20) // age in (10,20)
     member.age.notIn(10, 20) // age not in (10, 20)
     member.age.between(10,30) //between 10, 30
     member.age.goe(30) // age >= 30
     member.age.gt(30) // age > 30
     member.age.loe(30) // age <= 30
     member.age.lt(30) // age < 30
     member.username.like("member%") //like 검색
    member.username.contains("member") // like ‘%member%’ 검색 
    member.username.startsWith("member") //like ‘member%’ 검색 
```

# 집합 함수
JPQL이 제공하는 모든 집합 함수를 제공한다.
tuple은 프로젝션과 결과반환에서 설명한다.

# 결과 조회
- fetch( ) : 리스트 조회, 데이터 없으면 빈 리스트 반환
  - fetchOne( ) : 단 건조회
    -  결과가 없으면 : null
    - 결과가 둘 이상이면 : com.querydsl.core.NonUniqueResultException
- fetchFirst(): limit(1).fetchOne()
- fetchResults() : 페이징 정보 포함, total count 쿼리 추가 실행
- fetchCount() : count 쿼리로 변경해서 count 수 조회
- extracting(): 객체 리스트에서 특정 필드만 꺼내 검증
- containsExactly(...): 해당 컬렉션이 주어진 값들과 동일한 순서, 동일한 값

# 조인
조인의 기본 문법은 첫 번째 파라미터에 조인 대상을 지정하고, 두 번째 파라미터에 별칭(alias)으로 사용할 Q 타입을
지정하면 된다. join(조인 대상, 별칭으로 사용할 Q타입)
- 세타조인 : 연관관계가 없는 필드로 조인, from절에 엔티티를 나열하여 모든 엔티티를 가져오고 전부 조인 후 where절에서 필터링 진행
단, 세타 조인은 외부 조인이 불가능함으로 left out, right out 조인X 그럼으로 on을 사용하여 외부 조인 이용

# 조인 - on절
1. 조인 대상 필터링
   on 절을 활용해 조인 대상을 필터링 할 때, 외부조인이 아니라 내부조인(inner join)을 사용하면, where 절
   에서 필터링 하는 것과 기능이 동일하다. 따라서 on 절을 활용한 조인 대상 필터링을 사용할 때, 내부조인 이면 익
   숙한 where 절로 해결하고, 정말 외부조인이 필요한 경우에만 이 기능을 사용하자

2. 연관관계 없는 엔티키 외부 조인
  leftJoin()부분에 일반 조인과 다르게 엔티티 하나만 들어간다.
  leftJoin(member.team, team) vs  from(member).leftJoin(team).on(xxx)

# 조인 - 패치 조인
페치 조인은 SQL에서 제공하는 기능은 아니다. SQL조인을 활용해서 연관된 엔티티를 SQL 한번에 조회하는 기능이
다. 주로 성능 최적화에 사용하는 방법이다.
join(), leftJoin()등 조인 기능 뒤에 fetchJoin()이라고 추가하면 된다


# 서브 쿼리 : com.querydsl.jpa.JPAExpressions 사용한
1. eq
2. goe
3. in
   - from 절의 서브쿼리 한계
   JPA JPQL 서브쿼리의 한계점으로 from 절의 서브쿼리(인라인 뷰)는 지원하지 않는다. 당연히 Querydsl도 지원하지
   않는다. 하이버네이트 구현체를 사용하면 select 절의 서브쿼리는 지원한다. Querydsl도 하이버네이트 구현체를 사용
   하면 select 절의 서브쿼리를 지원한다.
   - from 절의 서브쿼리를 쓰는 경우는, sql을 데이터에 가져오는 것에 집중하고, 화면 데이터 포맷이나 랜더링 등은 화면에 역할을 부여
   - from 절의 서브쿼리 해결방안
     1. 서브쿼리를 join으로 변경한다. (가능한 상황도 있고, 불가능한 상황도 있다.)
     2. 애플리케이션에서 쿼리를 2번 분리해서 실행한다.
     3. nativeSQL을 사용한다.

# case 문 : select,  조건절(where), order by에서 사용 가능
복잡한 조건에서는 CaseBuilder() 사용, 변수로 선언 후 select절, orderBy 절에서 사용

# select 대상 지정(프로젝션) 결과 반환 - DTO로 프로젝션 결과 반환
프로젝션 대상이 하나면 타입을 명확하게 지정할 수 있지만 프로젝션 대상이 둘 이상이면 튜플이나 DTO로 조회
튜플을 사용시에는, 리포지토리단을 넘어서도 사용하는 것보다 의존되지 않도록 설게해야 함
import com.querydsl.core.types.Projections;
1. 프로퍼티 접근
   - 기본 생성자 필요
   - Projections.bean
2. 필드 직접 접근
   - getter, setter 필요 X 
   - Projections.fields
   - 이름이 다를 때 해결 방안 -> 
   - ExpressionUtils.as(JPAExpressions.서브 쿼리) 필드나, 서브 쿼리에 별칭 적용
3. 생성자 사용
    - DTO 생성자와 타입이 맞아야 한다
    - Projections.constructor

import com.querydsl.core.annotations.QueryProjection;
4. @QueryProjection을 DTO 사용하여 DTO도 Q파일 생성
    - DTO 생성자 필요
    -  컴파일러로 타입을 체크 가능
    - 여러 레이어에 사용되는 DTO 내부에 QueryDsl 의존성을 갖게 됨

# 동적 쿼리
1.  BooleanBuilder 사용
    - BooleanBuilder.and로 null 값 여부에 따라 where 조건 추가
    - 필수인 경우 new BooleanBuilder 생성 시 초기값으로 지정 가능
2.  Where 다중 파라미터 사용
    - 응답값으로 where에 null이 들어가면 무시된다
    -  BooleanExpression 메서드 조합으로 사용 시 반환 타입

# SQL function 호출하기
하이버네이트 구현체를 사용하는 경우, SQL function은 JPA와 같이 Dialect에 등록된 내용만 호출할 수 있다.
- replace 함수: (Expressions.stringTemplate("function('replace', {바꿀 것}, {기존}, {비뀐})" 


<오늘의 질문>
- JPQL에서 .getSingleResult()
- SQL 인젝션(SQL Injection) 공격
- startsWith, endsWith, contains 문자열을 비교
- 서브 쿼리 eq와 goe 차이
- Predicate와 BooleanExpression 차이


-----
# 실무 활용
# JPAQueryFactory 스프링 빈 등록
``` java
@Bean
JPAQueryFactory jpaQueryFactory(EntityManager em) {
return new JPAQueryFactory(em);
}
```

# 사용자 정의 리포지토리
1. 사용자 정의 인터페이스 작성
2. 사용자 정의 인터페이스 구현
3. 스프링 데이터 리포지토리에 사용자 정의 인터페이스 상속

# Querydsl 페이징 연동
- offset: 몇 번부터 시작, limit: 한 페이지에 가져올 갯수
- Querydsl이 제공하는 fetchResults()를 사용하면 내용과 전체 카운트를 한번에 조회할 수 있다.(실제 쿼리는 2번 호출)
- Page구현체인 PageImpl<>(results.getResults(), pageable,  results.getTotal());
- count 쿼리 최적화를 위해서는 fetchResults 사용하는 경우에서는 최적화가 안 됨으로 분리
- count 쿼리가 생략 가능한 경우 생략해서 처리: PageableExecutionUtils.getPage()로 최적화
  - 페이지 시작이면서 컨텐츠 사이즈가 페이지 사이즈보다 작을 때
  - 마지막 페이지 일 때 (offset + 컨텐츠 사이즈를 더해서 전체 사이즈 구함, 더 정확히는 마지막 페이지이면 서 컨텐츠 사이즈가 페이지 사이즈보다 작을 때)
  - countQuery에서는 .fetchCount()시에 쿼리가 날라가는 함수임으로

# 스프링 데이터 JPA가 제공하는 Querydsl
1. 인터페이스 지원-QuerydslPredicateExecutor
- findById, findAll 등 지원
- (묵시적 조인은 가능하지만 left join이 불가능하다.
- 다른 계층이 Querydsl에 의존
2. Querydsl Web 지원: @QuerydslPredicate 컨트롤러 사용 시 파라미터 바인딩을 predicate조건으로 받아준다
- QuerydslBinderCustomizer로 복잡한 내용에 대해 커스텀 작업을 해야 한다.
3. 리포지토리 지원 - 추상클래스 QuerydslRepositorySupport
- 엔티티 매니저 주입을 받아줌으로 getEntityManger()
- queryFactory사용 대신 from,whre,select순으로 사용
-  getQuerydsl().applyPagination(pageable,jpaQuery) 스프링 데이터가 제공하는 페이징을 편리하게 가능(offset,limit 대체)
- 파라미터 바인딩 되는 sort 불가/ 스프링 데이터 Qsort 가능하지만 조건 직접 처리해야 함
4. Querydsl 지원 클래스 직접 만들기 
- QuerydslRepositorySupport가 지닌 한계를 극복할 수 있다.

