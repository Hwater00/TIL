<h1>테스트 주도 개발 시작하기(최범균, 가메출판사)</h1>

# CH1 TDD JUnit 설정

## Gradle 프로젝트에서 JUnit 설정

JUnit 5의 Jupiter 의존성을 테스트 목적으로 추가해야 합니다.
또한, JUnit 5로 작성된 테스트 코드를 실행하기 위해 `useJUnitPlatform()`을 설정해야 합니다.

### build.gradle 설정 예시

```gradle
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.5.0'
}

test {
    useJUnitPlatform()
    testLogging {
        events 'passed', 'skipped', 'failed'
    }
}
```
---

# chap02
- JUint은  @Test 애노테이션을 분인 메서드를 테스트 메서드로 인긱한다. 테스트 메서드는 기능을 검증하는 코드를 담고 있는 메서드이다.
- assertEqulas() 메서드는 인자로 받은 두 값이 동일한지 비교한다. 이때 첫 번째 인자는 기대한 값이고 두 번째 인자는 실제 값이다. 비교한 결과 두 값이 동일하지 않으면 AssertionFailedError가 발생한다.
org.opentest4j.AssertionFailedError

---

## TDD 과정

테스트 → 코딩 → 리팩토링 반복

1. 테스트 작성
2. 테스트 실패 시, 테스트를 통과할 만큼만의 코드 작성
3. 테스트 통과 시, 코드 리팩토링 후 테스트 재실행하여 확인

### 테스트 코드 작성 순서

- 쉬운 경우에서 어려운 경우로 진행

  - 초반에 복잡한 테스트부터 시작하면 안 되는 이유

    1. 해당 테스트를 통과시키기 위해 한 번에 구현해야 할 코드가 많아진다.

    2. 한 번에 구현하는 시간이 짧아지면 디버깅할 때 유리하다. 작성한 코드가 많지 않고 작성 시간도 짧으면 코드에 대한 내용을 명확히 기억할 수 있다.

  - 예외적인 경우에서 정상인 경우로 진행

      - 예외 상황을 먼저 테스트하는 이유

      1. 예외 상황을 고려하지 않은 후 예외 상황을 처리하기 위해 조건문을 중복해서 추가하는 일이 발생하기 때문이다.

### TDD 완급 조절

1. 정해진 값을 리턴

2. 값 비교를 이용해서 정해진 값을 리턴

3. 다양한 테스트를 추가하면서 구현을 일반화

### ❓ 질문

Q1. 테스트 코드에 사용하는 ENUM은 교재 2장에서는 test 폴더 하위로 만들었는데, main 하위에 있던 ENUM과 같이 사용할 수 있는 걸까요?

→ test 하위에 있던 것을 import로 불러오기 불가했음.

### 테스트 케이스 예시
#### TDD 예시 : 암호 강도 검사
```java
// PasswordStrengthMeterTest 
public class PasswordStrengthMeterTest {

    private PasswordStrengthMeter meter = new PasswordStrengthMeter();
    private void assertStrength(String password, PasswordStrength passwordStrength){
        PasswordStrength result = meter.meter(password);
        assertEquals(passwordStrength, result);
    }

    // 4. 값이 없는 경우
    @Test
    void nullInput_Then_Invalid(){
        assertStrength(null, PasswordStrength.INVALID);
        assertStrength("", PasswordStrength.INVALID);
    }
}
```

### 전체 코드

#### PasswordStrength

```java
public enum PasswordStrength {
    STRONG, NORMAL, INVALID, WEAK
}
```

#### PasswordStrengthMeter

```java
public class PasswordStrengthMeter {

    public PasswordStrength meter(String s){
        // null 검사
        if(s == null || s.isEmpty()){
            return PasswordStrength.INVALID;
        }

        int metCount = getMetConditionCount(s);
        if(metCount <= 1) return PasswordStrength.WEAK;
        if(metCount == 2) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }

    private int getMetConditionCount(String s){
        int metCount = 0;
        if(s.length() >= 8) metCount++;
        if(meetContainingNumberCondition(s)) metCount++;
        if(meetContainingUppercaseCondition(s)) metCount++;
        return metCount;
    }

    private boolean meetContainingNumberCondition(String s){
        for(char ch : s.toCharArray()){
            if(ch >= '0' && ch <= '9'){
                return true;
            }
        }
        return false;
    }

    private boolean meetContainingUppercaseCondition(String s){
        for(char ch : s.toCharArray()){
            if(Character.isUpperCase(ch)){
                return true;
            }
        }
        return false;
    }
}
```

#### PasswordStrengthMeterTest

```java
public class PasswordStrengthMeterTest {

    private PasswordStrengthMeter meter = new PasswordStrengthMeter();
    private void assertStrength(String password, PasswordStrength passwordStrength){
        PasswordStrength result = meter.meter(password);
        assertEquals(passwordStrength, result);
    }

    @Test
    void meetAllCondition_Then_Strong(){
        assertStrength("ab12!@AB", PasswordStrength.STRONG);
        assertStrength("abc1!Add", PasswordStrength.STRONG);
    }

    @Test
    void meetOtherCondition_except_length_Then_Normal(){
        assertStrength("ab12!@A", PasswordStrength.NORMAL);
    }

    @Test
    void meetOtherCondition_except_number_Then_Normal(){
        assertStrength("ab!@ABqwer", PasswordStrength.NORMAL);
    }

    @Test
    void nullInput_Then_Invalid(){
        assertStrength(null, PasswordStrength.INVALID);
        assertStrength("", PasswordStrength.INVALID);
    }

    @Test
    void meetOtherCondition_except_uppercase_Then_Normal(){
        assertStrength("ab12!@cd", PasswordStrength.NORMAL);
    }

    @Test
    void meetOnlyLengthCondition_Then_Weak(){
        assertStrength("abcdefgh", PasswordStrength.WEAK);
    }

    @Test
    void meetOnlyNumberCondition_Then_Weak(){
        assertStrength("12345", PasswordStrength.WEAK);
    }

    @Test
    void meetOnlyUppercaseCondition_Then_Weak(){
        assertStrength("ABCDE", PasswordStrength.WEAK);
    }

    @Test
    void meetNoCondition_Then_Weak(){
        assertStrength("aaaaa", PasswordStrength.WEAK);
    }
}
```

# ch4 TDD.기능 명세.설계

1. 설계는 기능 명세로부터 시작한다
2. 입력과 결과로 구분한 기능 명세
- 입력
  - 아이디와 암호
  - 메서드의 파라미터로 전달
- 결과
  - 아이디와 암호가 일치하면 성공, 일치하지 않으면 실패
  - 리턴 값
  - 익셉션 결과
  - 변경 → DB에 데이터를 추가하는 것은 값을 리턴하는 것과 달리 시스템의 상태를 변경한다. 이런 변경은 리턴 값으로는 결과를 알 수 없기 때문에 테스트 대상을 실행한 뒤에는 변경 대상에 접근해서 결과를 확인해야 한다

### 설계 과정을 지원하는 TDD

TDD는 테스트 코드를 먼저 만들고 테스트를 통과시키기 위해 코드를 구현하고 리팩토링하는 과정을 반복한다.
- 테스트: A) 테스트 할 기능 실행 + B) 실행 결과 검증
  - A) 테스트 대상이 되는 클래스와 메소드 이름 결정, 메소드에서 사용할 파라미터 결정
  - B) 리턴값, 특정 Exception 발생


- 테스트 코드를 먼저 작성하기 위한 요소
  - 테스트할 기능을 실행
    - 테스트에서 실행할 수 있는 객체나 함수 존재
    - 클래스, 메서드 이름, 메서드 인자와 타입
  - 실행 결과를 검증

### 기능 명세 구체화

보통 개발자는 기획자가 작성한 스토리보드나 와이어프레임과 같은 형태로 요구사항 명세를 전달받는다. 이런 문서는 사용자나 기획자가 보기에는 적당할지 모르나 개발자가 기능을 구현하기에는 생략된 내용이 많다.

- 파라미터와 결과 값 결정→ 요구사항 문서에서 기능의 입력과 결과를 도출 → 테스트 사례를 추가하는 과정에서 구현의 애매한 점을 발견→ 기능 동작을 구체적으로 정리

## 🐛 내가 느낀 TDD !
최초 요구사항은 시간이 지나면서 변하는데, TDD는 미리 앞서서 코드를 만들지 않으므로 불필요한 구성 요소를 덜 만들게 된다

API 파라미터, 리턴 JSON구조 설계 후 변경이 많은 편이었는데 TDD 작성 시 설계 과정에서 고민하는 것이 겹치면서 TDD로 변경을 위한 단계가 추가되어 FE에 혼란을 감소시킬 수 있을 것 같다

명세를 최대한 구체적으로 설계하는 것이 개발자의 역량이라 생각하는데 기능 명세를 구체화할 수 있는 방법으로 TDD가 될 것 같다.

기능이 잘 구현됬는지 확인이 테스트 코드의 역할이라 생각했는데, 설계를 잘 하는 역할이구나 생각 변화가 일어났다.

---

# ch5 JUnit 5 기초

### JUnit 모듈 구성

- JUnit 플랫폼: 테스팅 프레임워크를 구동하기 위한 런처와 테스트 엔진을 위한 API를 제공
- JUnit 주피터(Jupiter): JUnit 5를 위한 테스트 API와 실행 엔진을 제공한다
- JUnit 빈티지(Vintage): JUnit 3과 4로 작성된 테스트를 JUnit 5 플랫폼에서 실행하기 위한 모듈을 제공한다.

JUnit 5는 테스트를 위한 API를 제공한다. 주피터 API를 사용해서 테스트를 작성하고 실행하려면 주피터 관련 모듈(junit-jupiter-engine과 junit-jupiter-api)을 의존에 추가하면 된다.

### @Test 애노테이션과 테스트 메서드

JUnit 코드의 기본 구조는 @Test 애노테이션 메서드부터

- 다른 클래스와 구분을 쉽게 하기 위해 ‘Test’ 접미사 붙이기 ex) SumTest
- @Test 애노테이션을 붙인 메서드는 private이면 안 된다
  - 이유: **private 메서드는 클래스 외부에서 접근할 수 없기 때문**
    - 기술적인 이유: 리플렉션을 이용해 테스트 메서드를 가져올 수 있기 때문입니다.
    - 의미상의 이유: 테스트는 실제로 돌아가는 모듈이나 단위 기능들을 점검하기 위한 것이므로, 테스트를 숨길 필요가 없습니다.
    - 공유의 의미: JUnit의 테스트 코드는 모든 개발자가 공유해야 하므로, 모든 사람에게 열려 있어야 합니다.
    - 직관성의 의미: 테스트용이라는 직관성을 위해 public으로 한 것입니다.

### 주요 단언 메서드 Assertions 클래스 제공

| 메서드 | 설명 |
| --- | --- |
| assertEquals(expected, actual) | 실제 값(actual)이 기대하는 값(expected)과 같은지 검사한다 |
| assertNotEquals(unexpected, actual) | 실제 값(actual)이 특정 값(unexpected)과 같지 않은지 검사한다 |
| assertSame(Object expected,Object actual) | 두 객체가 동일한 객체인지 검사한다 |
| assertNotSame(Object unexpected, Object actual) | 두 객체가 동일하지 않은 객체인지 검사한다 |
| assertTrue(boolean condition) | 값이 true인지 검사한다 |
| assertFalse(boolean condition) | 값이 false인지 검사한다 |
| assertNull(Object actual) | 값이 null인지 검사한다 |
| assertNotNull(Object actual) | 값이 null이 아닌지 검사한다 |
| fail() | 테스트 실패를 처리한다 |
| assertThrows(Class<T> expectedType, Executable executable) | executable을 실행한 결과로 지정한 타입의 익셉션을 발생하는지 검사한다 |
| assertDoesNotThrow(Executable executable) | executable을 실행한 결과로 익셉션이 발생하지 않는지 검사한다 |

```java
assertThrows(IllegalArgumentException.class, () -> {
    AuthService authService = new AuthService();
    authService.authenticate(null, null);
});
```

### @BeforeEach 애노테이션과 @AfterEach 애노테이션

JUnit 각 테스트 메서드 코드 실행 순서

1. 테스트 메서드를 포함한 객체 생성
2. (존재하면) @BeforeEach 애노테이션이 붙은 메서드 실행
3. @Test 애노테이션이 붙은 메서드 실행
4. (존재하면) @AfterEach 애노테이션이 붙은 메서드 실행

```java
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LifecycleTest {
    public LifecycleTest() {
        System.out.println("new LifecycleTest");
    }

    @BeforeEach
    void setUp() {
        System.out.println("setUp");
    }

    @Test
    void a() {
        System.out.println("A");
    }

    @Test
    void b() {
        System.out.println("B");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearDown");
    }
}

// 결과
new LifecycleTest
setUp
A
tearDown
new LifecycleTest
setUp
B
tearDown
```

---

# ch6 테스트 코드의 구성

- 테스트 코드는 기능을 실행하고 그 결과를 확인하므로 상황, 실행, 결과 확인의 세 가지 요소로 테스트를 구성할 수 있다. ⇒ given, when, then
- 외부 상태가 테스트 결과에 영향을 주지 않도록 할 것 (*외부 상태: 파일, DBMS, 외부 서버 등)

### 외부 REST API 고려사항

- REST API 응답 결과가 유효한 계좌 번호인 상황
- REST API 응답 결과가 유효하지 않은 계좌 번호인 상황
- REST API 서버에 연결할 수 없는 상황
- REST API 서버에서 응답을 5초 이내에 받지 못하는 상황

---

# ch 7 대역

### 대역의 필요성

- 테스트 코드에서 제어하기 어려운 외부 요인이 있을 경우?
  - ex) 외부 환경을 테스트 환경에 맞추기 어려운 경우, 테스트 코드에서 생성한 외부 결과를 마음대로 초기화할 수 없는 경우
  - → 해결 방안: '**대역**'을 사용하여 테스트 대상이 의존하는 외부 상황이나 결과를 대체할 수 있다.
- 외부 요인이 테스트에 관여하는 경우
  - 1) 테스트 대상에서 파일 시스템을 사용
  - 2) 테스트 대상에서 DB로부터 데이터를 조회하거나 데이터를 추가
  - 3) 테스트 대상에서 외부의 HTTP 서버와 통신
  - 예시: 자동이체
    - 상황: 외부 업체가 제공하는 API를 이용해서 카드번호가 유효한지 확인하고 그 결과에 따라 자동이체 정보를 등록하는 기능
    - 문제: 외부 업체에서 정상 카드번호, 만료일이 지난 카드번호 등 상황별 카드번호를 제공하지 않으면 테스트를 진행할 수 없다.

테스트 대상에서 의존하는 요인 때문에 테스트가 어려울 때는 외부 요인을 대신하는 **대역**을 써서 테스트를 진행할 수 있다.

대역을 사용하여 AutoDebitRegister에 대한 테스트를 수행한다.

- 외부 카드 정보 API 연동
  - StubCardNumberValidator : 카드 정보 API 연동을 대신하여 유효한 카드번호, 도난 카드번호와 같은 상황을 대신한다.
- 자동이체 정보를 저장한 DB
  - MemoryAutoDebitInfoRepository : 특정 사용자에 대한 자동이체 정보가 이미 등록되어 있는 상황, 등록되어 있지 않은 상황을 대신한다.

- 제어가 힘든 외부 상황에서 의존 도출

  - 제어하기 힘든 외부 상황을 별도 타입으로 분리
  - 테스트 코트는 별도로 분리한 타입의 대역을 생성
  - 생성한 대역을 테스트 대상의 생성자 등을 이용해서 전달
  - 대역을 이용해서 상황 구성

### 대역의 종류

| **종류** | **설명** |
| --- | --- |
| 스텁 (Stub) | - 구현을 단순한 것으로 대체, 테스트에 맞게 단순히 원하는 동작을 수행 |
| 가짜 (Fake) | - 제품에는 적합하지 않지만, 실제 동작하는 구현을 제공 |
| 스파이 (Spy) | - 호출된 내역 기록, 기록한 내용은 테스트 결과 검증 시 사용 |
| 모의 (Mock) | - 기대한 대로 상호작용 하는지 행위 검증<br>- 기대한 대로 동작하지 않으면 Exception 발생 |

### 대역 사용 실습 : 회원가입 기능

- UserRegister : 회원가입에 대한 핵심 로직 수행
- WeakPasswordChecker : 암호가 약한지 검사 (→ 약하다면 회원가입 실패)
- UserRepository : 회원 정보 저장, 조회 기능 제공
- EmailNotifier : 이메일 발송 기능 제공

- 암호의 강도 확인 ⇒ Stub
  - 암호가 약하다면 가입 실패
- repository를 가짜 구현으로 사용 ⇒ Fake
  - 이미 가입된 ID로 가입을 시도할 경우 가입 실패, Exception 발생
  - 중복 ID가 존재하지 않을 경우 가입 성공
- 이메일 발송 여부 확인 ⇒ Spy
  - 가입 성공 시, 이메일로 가입 안내 메일 발송

### 모의 객체로 스텁과 스파이 대체 (Mockito)

- 모의 객체를 위한 도구 중 **Mockito**를 사용

- **Mock, Mockito**

  **Mock (모의 객체)**

  - 실제 객체 대신 테스트를 위해 사용하는 가짜 객체
  - 테스트 대상 코드가 외부 의존성(DB, API 등)을 직접 호출하지 않도록 대체하는 역할

    ---

  **Mockito**

  - Mock 생성, 동작 설정 및 검증, 스텁을 지원하는 프레임워크
  - 의존성 추가
      ```
      org.mockito:mockito-core:2.26.0
      ```

- Mockito의 ArgumentCaptor는 모의 객체를 메소드를 호출할 때 전달한 객체를 담는 기능을 제공한다.
- BDDMockito.then( ).should( )로 모의 객체의 메소드가 호출됐는지 확인할 때 ArgumentCaptor의 capture( ) 메소드를 사용하면 메소드를 호출할 때 전달한 인자가 ArgumentCaptor에 담긴다.

  →  ArgumentCaptor의  getValue( )  메소드를 통해 보관한 인자를 구하고 이를 검증에 활용할 수 있다.

### 모의 객체는 과하게 사용하지 말자.

- 모의 객체를 이용하면 처음에는 대역 클래스를 만들지 않아도 돼서 편할 수 있지만, 결과 값을 확인하는 수단으로 모의 객체를 사용하기 시작하면 결과 검증 코드가 길어지고 복잡해진다.
- 모의 객체의 메소드 호출 여부를 결과 검증 수단으로 사용하는 것은 주의해야 한다.
- 특히, 저장소에 대한 대역은 메모리를 이용한 가짜 구현을 사용하는 것이 테스트 코드 관리에 유리하다.

---

# ch 8 테스트 가능한 설계

### 테스트가 어려운 코드 & 테스트 가능한 설계

- 하드 코딩된 경우
  - 문제) 파일 경로, IP 주소, 포트번호 등이 하드코딩 된 경우 테스트가 어렵다.
  - 해결) 하드 코딩된 상수를 교체할 수 있는 기능 추가하기

- 의존 대상을 직접 생성한 경우
  - 문제) 의존 대상이 올바르게 동작하는데 필요한 환경을 모두 구성하기 어렵다.
  - 해결) 의존 대상을 주입 받을 수 있는 수단을 제공하여 교체할 수 있도록 한다.
- 정적 메서드 사용 → 외부 라이브러가 정적 메서드를 제공한다면 외부 라이브러리 직접 사용이 아닌 연동하기 위한 타입을 따로 만든다.
- 실행 시점에 따라 달라지는 코드 → 시간이나 임의 값 생성 기능 분리하기
- 역할이 섞여 있는 코드
- 콘솔에서 입력을 받거나 결과를 콘솔에 출력한다
- 테스트 대상이 사용하는 의존 대상 클래스나 메서드가 final이다. 이 경우 대역으로 대체가 어려울 수 있다.
- 테스트 대상의 소스를 소유하고 있지 않아 수정이 어렵다
- 매서드 중간에 소켓 통신 코드가 포함되어 있다→ 소켓 통신이나 HTTP 통신은 실제를 대체할 서버를 로컬에 띄워서 처리할 수 있다, 서버 수준에서 대역을 사용한다고 생각하면 된다

---

# ch 9 테스트 범위와 종류

### 테스트 범위

#### 기능 테스트

사용자 입장에서 시스템이 제공하는 기능이 올바르게 동작하는지 확인한다. 이 테스트를 수행하려면 시스템을 구동하고 사용하는대 필요한 모든 구성요소가 필요하다.  끝에서 씉까지 모든 구성요소를 논리적으로 완전한 하나의 기능으로 다루기에 E2E 테스크로도 볼 수 있다

- QA 조직에서 수행하는 테스트가 주로 기능 테스트이다
- 시스템이 필요로 하는 데이터를 입력하고 결과가 올바른지 확인한다

#### 통합 테스트

시스템의 각 구성 요소가 올바르게 연동되는지 확인한다. 통합 테스트는 소프트웨어의 코드를 직접 테스트한다.

일반적인 웹 어플리케이션은 프레임워크, 라이브러리, 데이터베이스, 구현한 코드가 주요 통합 테스트 대상이다

- 기능 테스트와 비교) 기능 테스트는 앱을 통해 가입 기능을 테스트한다면 통합 테스트는 서버의 회원 가입 코드를 직접 테스트하는 식이다.
- 스프링 프레임워크나 마이바티스 설정이 올바른지, SQL 쿼리가 맞는지, DB 트랜잭션이 잘 작동하는지 등을 검증할 수 있다.

#### 단위 테스트

개별 코드나 컴포넌트가 기대한대로 동작하는지 확인한다. 한 클래스나 한 메서드와 같은 작은 범위를 테스트한다. 일부 의존 대상은 스텁이나 모의 객체 등을 이용해서 대역으로 대체한다

- 책에서 살펴본 테스트가 단위 테스트

### WireMock을 이용한 REST 클라이언트 테스트

- **Mock Server, WireMock, WireMockServer**

  **Mock Server**

  - 테스트를 위해 실제 서버처럼 동작하는 가짜 서버

  **WireMock**

  - Java에서 Mock Server를 쉽게 만들 수 있도록 도와줌
  - 의존성 추가
      ```
      org.wiremock:wiremock-standalone:3.3.1
      ```

- WireMockServer는 HTTP 서버를 흉내낸다.
- 테스트 실행 전, WireMockServer를 시작한다. 실제 HTTP 서버가 뜬다.
- 테스트에서 WireMockServer의 동작을 기술한다.
- HTTP 연동을 수행하는 테스트를 실행한다.
- 테스트 실행 후, WireMockServer를 중지한다.

---
# ch10 테스트 코드와 유지보수

- 테스트 코드도 그 자체로 코드이기 때문에, 제품 코드와 동일하게 유지보수 대상이 된다.
- 테스트 코드 자체의 유지보수성이 좋아야 한다.

### ⚠️ 테스트 코드를 만들 때 주의해야 할 사항 ⚠️

1. **단언의 기댓값은 변수나 필드를 사용하여 표현하지 말고 실제 값 사용하기**
  - 단언할 때 사용한 값이 무엇인지 알기 위해 코드를 왔다갔다 할 필요가 없어진다.

    ```java
    // 안 좋은 예
    @Test
    void dateFormat() {
        LocalDate date = LocalDate.of(1945, 8, 15);
        String dateStr = formatDate(date);
        assertEquals(date.getYear() + "년 " + date.getMonthValue() + "월 " + date.getDayOfMonth() + "일", dateStr);
    }
    
    // 좋은 예
    @Test
    void dateFormat() {
        LocalDate date = LocalDate.of(1945, 8, 15);
        String dateStr = formatDate(date);
        assertEquals("1945년 8월 15일", dateStr);
    }
    ```

2. **두 개 이상을 검증하지 않기**
  - 한 테스트에서 검증하는 내용이 2개 이상이면, 테스트 결과를 확인할 때 집중도가 떨어진다.
  - 한 테스트 메소드에서 서로 다른 내용을 검증한다면, 각 검증 대상을 별도로 분리할 것.
  - 테스트 메소드가 반드시 한 가지만 검증해야 하는 것은 아니지만, 검증 대상이 명확하게 구분된다면 테스트 메소드도 구분하는 것이 유지보수에 유리하다.

3. **모의 객체는 가능한 범용적인 값을 사용**
  - 테스트 의도를 해치지 않는 범위에서, ‘pw’와 같은 특정한 값보다는, **`Mockito.anyString()`** 과 같은 범용적인 것을 사용해야 한다.
    - 테스트 코드를 수정할 때, 모의 객체 관련 코드도 함께 수정해야 하는 빈도도 줄어든다.

      ```java
      @DisplayName("약한 암호이면 회원가입 실패")
      @Test
      void weakPassword() {
          // 모의 객체를 이용하여 Stub을 대신
          BDDMockito
              .given(mockPasswordChecker.checkPasswordWeak(Mockito.anyString()))
              .willReturn(true);

          assertThrows(WeakPasswordException.class, () -> {
              userRegister.register("id", "pw", "email");
          });
      }
      ```

      - @Sql 애노테이션을 사용해서 테스트실행 전에 특정 쿼리를실행 → 모든 케스트가 같은 값을 사용하는 데이터
        #### SQL 저장 파일 내용 (`init-data.sql`)

        ```
        -- SQL 파일 내용
                truncate table user;
                insert into user values('cbk','pw','cbk@cbk.com');
                insert into user values('tddiht','pw1','tdhot@ilovetdd.com');
        ```
        ### Spring Boot 테스트 클래스
        ```
        @SpringBootTest
        @Sql("classpath:init-data.sql")
        public class UserRegisterIntTestUsingSql {
        @Autowired
        private UserRegister register;
        @Autowired
        private JdbcTemplate jdbcTemplate;
      
        @Test
        void 동일한ID가_이미_존재하면_익셉션() {
         // 실행 결과 확인
         assertThrows(DupIdException.class,
         () -> register.register("cbk", "strongpw", "email@email.com")
         );
        }
      
        @Test
        void 존재하지_않으면_저장함() {
          // 실행
         register.register("cbk2", "strongpw", "email@email.com");
          } 
        }
        ```
4. **내부 구현보다 실행 결과을 검증**
  - 테스트 대상의 내부 구현을 검증하는 것이 나쁜 것은 아니지만, 구현을 조금만 변경해도 테스트가 깨질 가능성이 커진다.
  - 테스트 코드는 내부 구현보다 실행 결과를 검증해야 한다. (내부 구현은 언제든지 변경될 수 있으니까)

5. **셋업을 이용하여 여러 메소드에 동일한 상황 적용하지 않기**
  - 코드 중복을 제거하기 위해 `@BeforeEach`를 사용하여 상황을 구성할 수 있다.
  - 각 테스트 메소드를 위한 상황을 셋업 메소드에서 모두 따로 설정하는 건 NO.
  - 테스트 메소드는 자체적으로 검증하는 내용을 완전히 기술하고 있어야, 테스트 코드를 유지보수하는 노력을 줄일 수 있다.

   예시:
  ```java
public class UserGivenHelper {
    private JdbcTemplate jdbcTemplate;

    public UserGivenHelper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void givenUser(String id, String pw, String email) {
        jdbcTemplate.update(
            "insert into user values (?, ?, ?) " +
            "on duplicate key update password = ?, email = ?",
            id, pw, email, pw, email);
    }
}

@Autowired
JdbcTemplate jdbcTemplate;
private UserGivenHelper given;

@BeforeEach
void setUp() {
    given = new UserGivenHelper(jdbcTemplate);
}

@Test
void dupId() {
    given.givenUser("lee", "pw", "lee@email.com");

    assertThrows(DupIdException.class, () -> register.register("lee", "pw", "lee@email.com"));
}
```

6. **실행 환경이 다르다고 실패하지 않기**
  - 같은 테스트 메소드가 실행 환경에 따라 성공하거나 실패하면 안 된다. (ex: 파일 경로)
    - 파일 경로의 경우, 프로젝트 폴더를 기준으로 상대 경로로 설정할 것.

7. **실행 시점이 다르다고 실패하지 않기**
  - 테스트 코드에서 시간을 명시적으로 제어하는 것이 좋다.

      ```
    // 안 좋은 예
    public class Member {
        private LocalDateTime expiryDate;

        public boolean isExpired() {
            return expiryDate.isBefore(LocalDateTime.now());
        }
    }

    @Test
    void notExpired() {
        LocalDateTime expiry = LocalDateTime.of(2019, 12, 31, 0, 0, 0);
        Member member = Member.builder().expiryDate(expiry).build();
        assertFalse(member.isExpired());
    }
    ```

   
``` //좋은 예 1: 시간을 파라미터로 전달 받아 비교하기
public class BizClock {
    private static BizClock DEFAULT = new BizClock();
    private static BizClock instance = DEFAULT;

    public static void reset() {
        instance = DEFAULT;
    }

    public static LocalDateTime now() {
        return instance.timeNow();
    }

    protected void setInstance(BizClock bizClock) {
        BizClock.instance = bizClock;
    }

    public LocalDateTime timeNow() {
        return LocalDateTime.now();
    }
}

class TestBizClock extends BizClock {
    private LocalDateTime now;

    public TestBizClock() {
        setInstance(this);
    }

    public void setNow(LocalDateTime now) {
        this.now = now;
    }

    @Override
    public LocalDateTime timeNow() {
        return now != null ? now : super.now();
    }
}
```
   

8. **필요하지 않은 값은 설정하지 않기**
  - 중복 아이디를 가진 회원은 가입할 수 없다는 것을 검증하기 위해 테스트 코드를 짤 때, id가 아닌 email, password 등 필요하지 않은 값은 설정할 필요 없다.

9. **조건부로 검증하지 않기**
  - 조건에 따라서 단언을 하도록 하지 말 것 (테스트는 성공하거나 실패해야 한다! 아무 일도 일어나지 않으면 안돼…)

10. **통합 테스트는 필요하지 않은 범위까지 연동하지 않기 (`@JdbcTest`)**
  - `@SpringBootTest` 어노테이션을 사용하면 서비스, 컨트롤러 등 모든 스프링 빈을 초기화 함.
  - `@JdbcTest` 어노테이션을 사용하면 DataSource, JdbcTemplate 등 DB 연동과 관련된 설정만 초기화하고 다른 빈은 생성하지 않으므로 스프링 초기화 시간을 줄일 수 있다.
    ```java
    @JdbcTest
    @AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replalce.NONE)
    public class MemberDaoJdbcTest{
    @Autowired
    jdbcTemplate jdbcTemplate;
    
        private MemberDao doa;
        
        @BeforeEach
        void setUp(){
            dao = new MemberDao(jdbcTemplate);
        }
        
        @Test
        void findAll(){
        }
    
    }
    ```

11. **더 이상 쓸모 없는 테스트 코드는 삭제하기**
  - 단체 테스트 커버리지를 높이기 위한 목적으로 작성한 테스트 코드는 유지할 필요 없다.
---

# ch11 마치며

### TDD의 필요성

- 일정 압박으로 인해 본인이 만든 테스트 코드를 충분히 테스트하지 않고 다음 기능을 구현하게 되면, 버그를 포함할 가능성이 커진다.
- 테스트 코드를 작성하는 것이 개발 시간을 늘리는 것처럼 보이지만, 오히려 뒤로 갈수록 반복되는 테스트 시간을 줄여 전체 개발 시간(코딩 시간, 테스트 시간, 디버깅 시간)이 줄어든다.

### TDD를 적용할 때의 장점

- 테스트를 먼저 작성해야 한다면, 적어도 해당 테스트를 통과한 만큼은 코드를 올바르게 구현했다는 사실을 알 수 있다.
- 회귀 테스트로 사용할 수 있다.
- 버그가 발생할 가능성이 줄어든다.
- 버그 수정이 더 쉬워진다.

> **레거시 코드**처럼 TDD를 하기 힘든 코드는 테스트 코드라도 만들어봐라!
- 테스트 코드를 만들기 힘들다면, 약간의 위험을 감수하더라도 일부 코드를 리팩토링하여 테스트 코드를 만들 수 있는 구조로 변경해야 한다.

---
# 부록 C Mockito  기초 사용법

### 의존 설정

```java
dependencis{
	testImplementation('org-mockito:mockito-core:2.26.0')
}
```

### 모의 객체 생성

Mockito.mock() 메서드를 이용하면 특정 타입의 모의 객체를 생성할 수 있다

```java
import static org.mockito.Mockito.mock;

public class GameGenMockTest{
	@Test
	void mockTest(){
		GameNumGen genMock = mock(GameNumGen.class);
	}
}
```

### 스텁 설정

```java
import static org.juint.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class GameGenMockTest{
	@Test
	void mockTest(){
		GameNumGen genMock = mock(GameNumGen.class);
		**given(genMock.generate(GameLevel.EASY)).willReturn("123");**
		**given(fenMock.generate(null)).weillThrow(ILLefalArgumentException.class);**
		
		String num = genMock.generate(GameLEvel.EASY);
		assertEquals("123", num);
	}
}
```

### 인자 매칭 처리

org.mockito.ArgumentMatchers 클래스를 사용하여 임의의 값에 일피하도록 설정할 수 있다.

- ArgumentMatchers 클래스

    | anyInt(), anyShort(), anyLong(), anuByte(), anyChar(), anuDouble(), anyFloat, anyBoolen()  | 기본 데이터 타입에 대한 임의 값 일치 |
    | --- | --- |
    | anyString() | 문자열에 대한 임의 값 일치 |
    | any() | 임의 타입에 대한 일치 |
    | anyList() , anySet() , anyMap(), anyColletion() | 임의 콜렉션에 대한 일치 |
    | matches(String) , matches(Pattern) | 정규표현식을 이용한 String 값 일치 여부 |
    | eq(값) |  특정 값과 일치 |

Mockito는 한 인자라도 ArgumentMatcher를 사용해서 설정한 경우 모든 인자를ArgumentMatcher를 이용해 설정하도록 한다. 임의의 값과 일치하는 인자를 함께 사용하고 싶다면 ArgumentMatcher.eq()메서드 사용

```java
import static org.junit.jupiter.api.Assertiona.assertEquals;
import static org.mockito.ArgumentMatcher.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class AnyMatchTest{
	@Test
	void anyMatchTest(){
		GameNumGen genMock= mock(GameNymGen.class);
		**given(genMock.generate(any()))).willReturn("456");**
		
		String num = genMock.genrate(GameLevel.EASY);
		assertEquals("456", num);
		
		String num2 = genMock.generate(GameLEvel.NORMAL);
		assertEquals("456", num2);
	}
}
```

### 메서드 호출 검증

- 호출 횟수 검증 메서드

    | only() | 한 번만 호출 |
    | --- | --- |
    | times(int) | 지정한 횟수만큼 호출 |
    | never() | 호출하지 않음 |
    | atLeast(int) | 적어도 지정한 횟수만큼 호출 |
    | atLeastOnce() | atLeast(1)과 동일 |
    | atMost(int) | 최대 지정한 횟수만큼 호출  |

```java
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class GameTest{
	@Test
	void init(){
		GameNumGen genMock = mock(GameNumGen.class);
		Game game = new Game(genMock);
		game.init(GameLEvel.EASY)l
		
		**then(genMock).should(only()).genrate(GameLevel.EASY);**

	}
}
```

---
# 부록A JUnit 5

Junit5는 조건에 따라 테스트를 실행할지 여부를 결정하는 기능을 제공한다

이들 애노테이션은 org.junit.jupiter.api.condition 패키지에 속해 있다.

- 자바 버전에 따라 테스트

  @EnabledOnJre 애노테이션을 사용

- 시스템 프로퍼티 값에 따라 테스트

  @EnabledIfSystemProperty 애노테이션


### 태깅과 필터링

```java
test{
	useJUintPlatform{
		include Tags 'integration'
		exclude Tags 'slow| very-slow'
	}
}
```

@Tag 애노테이션은 테스트에 태그를 달 때 사용한다. 클래스와 테스트 메서드에 적용할 수 있다

- 태그 이름 규칙
    - null이나 공백이면 안 된다
    - 좌우 공백을 제거한 뒤에 공백을 포함하면 안 된다
    - ISO 제어 문자를 포함하면 안 된다
    - , (),& |! 포함하면 안 된다

### @Nested 애노테이션 중첩 구성

@Nested 애노테이션을 사용하면 중첩 클래스에 테스트 메서드를 추가 할 수 있다

중첩된 클래스는 내부 클래스이므로 외부 클래스의 멤버에 접근할 수 있다.

```java
import org.junit.jupitrt.api.Nested;

public class Outer{
	@BeforeEach void outerBefore(){}
	@Test void outer(){}
	@AfterEach void outerAfter() {}
	
	@Nested
	class NestedA{
		@BeforeEach void nestedBefore(){}
		@Test void nested(){}
		@AfterEach void nestedAfter() {}
	}
}

//실행 순서
1. Outer 객체 생성
2. NestedA 객체 생성
3. outerBefore() 메서드 실행
4. nestedBefore() 메서드 실행
5. nested1() 테스트 실행
6. nextedAfter() 메서드 실행
7. outerAfter() 메서드 실행
```

### @TempDir 애노테이션을 이용한 임시 폴더 생성

Junit 5.4 버전에 추가된 @TempDir 애노테이션을 필드 또는 라이프사이클 관련 메서드나 테스트 메서드의 파라미터로 사용하면 Junit은 임시 폴더를 생성하고 @TempDir 애노테이션을 붙인 필드나 파라미터에 임시 폴더 경로를 전달한다

File이나 Path 타입에 적용할 수 있다.

```java
import org.junit.jupiter.api.Test;
import org.junit.jupitrt.api.TempDir;
import java.io.File;

public class TempDirTest{
	@TempDir
	File tempFolder;
	
	@Test
	void fileTest(){
		//1.  tempFolder에 파일 생성 등 작업
	}
	
	//2. 특정 메서드에서만 임시 폴터 생성해서 사용
	@Test
	void fileRest(@TempDir Path TmpFolder){
	// 테스트 코드
	}
}

//3. 특정 클래스 단위로 임시 폴더 생성해서 사용
정적 필드  또는 @BeforeAll 메서드의 파라미터에 애노테이션 붙이기
@TempDir
static void tempFolderPerClass;
@BeforeAll
static void setUp(@TempDir File tempFolder
```

### @Timeout 애노테이션을 이용한 테스트 실행 시간 검증

Junit 5.5 버전부터 지원하는 애노테이션으로 테스트가 일정 시간 내에 실행되는지 검증할 수 있다.

```java
// 1초 이내에 테스트 메서드가 실행되는지 검증 코드
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class TimeoutTest{
	@Test
	@Timeout(1) // 초 단위 검증
	void sleep2seconds() throws InteruptedException{
		Thread.sleep(2000);
	}
	
	
	@Test
	@Timeout(value = 500, unit = TimeUnit.MILISECONDS) // 초 제외 단위 검증- unit속성 TimeUnit 값 지정
	void sleep40Mills throws InteruptedException{
		Thread.sleep(40);
	}
}
```
---
# 부록 B Junit 4 기초
## Junit4와 5 차이점

- 애노테이션 차이
    - JUnit4 @Before → JUnit5 @BeforeEach
    - JUnit4 @After → JUnit5 @AfterEach
- org.junit.Assert 클래스 차이
    - JUnit4는 assertAll()과 assertThrows()를 제공하지 않는다
- 익셉션 객체 추가 검증 차이
    - expected 속성을 사용한다
    - 단, 발생한 익셉션 객체를 사용하여 추가적 검증시에는 사용할 수 없음으로 try-catch로 직접 검증 처리

---
# 부록 D AssertJ 소개

- 의존 설정

```java
testCompile("org.asssertj:assertj-core:3.11.1");
```

AssertJ 사용 장점

- 개발 도구의 자동 완성 기능 활용
- 타입별로 다양한 검증 메서드를 제공
- 테스트 실패한 이유를 포함한 오류 메시지

```java
assertTrue(id.contains("a:); -> asserThat(id).contains("a");
```

## AssertJ 기본 사용법


assertThat(실제값).검증메서드(기대값)

assertThat() 메서드는 org.assertj.core.api.Assertions 클래스에 정적 메서드로 정의되어 있다. 주요 타입별로 assertThat() 메서드가 존재하며 타입에 따라 다른 검증 메서드를 제공한다

### 기본 검증 메서드

|  | isEqualTo(값) | 값과 같은지 검증한다 |
| --- | --- | --- |
|  | isNotEqualTo(값) | 값과 같지 않은지 검증한다 |
|  | isNull() | null인지 검증한다 |
|  | isNotNull() | null 아닌지 검증한다 |
| 가변 인자로 주거나 List 타입(정확하게는 Iterable을 구현하는 타입)을 이용해서 전달 | isIn(값 목록) | 값 목록에 포함되어 있는지 검증한다
|
| 가변 인자로 주거나 List 타입(정확하게는 Iterable을 구현하는 타입)을 이용해서 전달 | isNotIn(값 목록) | 값 목록에 포함되어 있지 않은지 검증한다
|
| Comparable 인터페이스를 구현한 타입이나 int, double과 같은 숫자 타입의 검증 | isLessThan(값) | 값보다 작은지 검증한다 |
|  | isLessThanOrEqual(값) | 값보다 작거나 같은지 검증 |
|  | isGreaterThan(값) | 값보다 큰지 검증 |
|  | isGreaterThanOrEqualTo( 값) | 값보다 크거나 같은지 검증 |
|  | isBetween(값1, 값2) | 값1, 값2 사이에 포함되어 있는지 검증 |
| Boolean, boolean 타입의 검증 | isTrue() | 값이 true인지 검증한다 |
|  | isFalse() | 값이 false인지 검증한다 |

### String에 대한 추가 검증 메서드

| 특정  값을 포함 | contains(CharSequence… values) | 인자로 지정한 문자열들을 모두 포함하고 있는지 검증 |
| --- | --- | --- |
|  | containsOnlyOnce(CharSequence charSequence) | 해당 문자열을 딱 한 번만 포함하는지 검증 |
|  | containsOnlyDigits( ) | 숫자만 포함하는지 검증 |
|  | containsWhitespaces( ) | 공백 문자를 포함하고 있는지 검증 |
|  | containsOnlyWhitespaces( ) | 공백 문자만 포함하는지 검증 |
|  | containsPattern(CharSequence regex)
containsPattern(Pattern pattern) | 지정한 정규 표현식에 일치하는 문자를 포함하는지 검증 |
| 특정 값을 포함하지 
않는 여부 | doesNotContain(CharSequence… values) | 인자로 지정한 문자열들을 모두 포함하고 있지 않은지 검증 |
|  | doesNotContainAnyWhitespaces( ) | 공백 문자를 포함하고 있지 않은지 검증 |
|  | doesNotContainOnlyWhitespaces( ) | 공백 문자만 포함하고 있지 않은지 검증 |
|  | doestNotContainPattern(CharSequence regex)
doestNotContainPattern(Pattern pattern) | 정규 표현식에 일치하는 문자를 포함하고 있지 않은지 검증 |
| 특정 문자열로 시작하거나 끝나는지 | startsWith(CharSequence prefix) | 지정한 문자열로 시작하는지 검증 |
|  | doestNotstartWith(CharSequence prefix) | 지정한 문자열로 시작하지 않는지 검증 |
|  | endsWith(CharSequence suffix) | 지정한 문자열로 끝나는지 검증 |
|  | doestNotendWith(CharSequence suffix) | 지정한 문자열로 끝나지 않는지 검증 |
|  |  |  |

### 숫자에 대한 추가 검증 메서드

| isZero( )
isNotZero( ) | 0인지 검증
0이 아닌지 검증 |
| --- | --- |
| isOne( ) | 1인지 검증 |
| isPositive( )
isNotPositive( ) | 양수인지 검증
양수가 아닌지 검증 |
| isNegative( )
isNotNegative( ) | 음수인지 검증
음수가 아닌지 검증 |

### 날짜/시간에 대한 검증 메서드

| LocalDateTime, LocalDate, Date 등 날짜와 시간 관련된 타입 | isBefore(비교할 값) | 비교할 값보다 이전인지 검증 |
| --- | --- | --- |
|  | isBeforeOrEqualTo(비교할 값) | 비교할 값보다 이전이거나 같은지 검증 |
|  | isAfter(비교할 값) | 비교할 값보다 이후인지 검증 |
|  | isAfterOrEqualTo(비교할 값) | 비교할 값보다 이후이거나 같은지 검증 |
| LocalDateTime, OffsetDateTime, ZonedDateTime 타입 | isEqualToIgnoringNanos(비교할 값) | 나노 시간을 제외한 나머지 값이 같은지 검증 ⇒ 초 단위까지 값이 같은지 검증 |
|  | isEqualToIgnoringSeconds(비교할 값) | 초 이하 시간을 제외한 나머지 값이 같은지 검증⇒ 분 단위까지 값이 같은지 검증 |
|  | isEqualToIgnoringMinutes(비교할 값) | 분 이하 시간을 제외한 나머지 값이 같은지 검증⇒ 시 단위까지 값이 같은지 검증 |
|  | isEqualToIgnoringHours(비교할 값) | 시 이하 시간을 제외한 나머지 값이 같은지 검증⇒ 일 단위까지 값이 같은지 검증 |

### 콜렉션에 대한 검증 메서드

| List/Set | hasSize(int expected) | 컬렉션의 크기가 지정한 값과 같은지 검증 |
| --- | --- | --- |
|  | contains(E … values) | 컬렉션이 지정한 값을 포함하는지 검증 |
|  | containsOnly(E … values) | 컬렉션이 지정한 값만 포함하는지 검증 |
|  | containsAnyOf(E … values) | 컬렉션이 지정한 값 중 일부를 포함하는지 검증 |
|  | containsOnlyOnce(E … values) | 컬렉션이 지정한 값을 한 번만 포함하는지 검증 |
| Map | containsKey(K key) | Map이 지정한 키를 포함하는지 검증 |
|  | containsKeys(K… keys)containsOnlyKeys(K… keys)doesNotContainKeys(K… keys) | Map이 지정한 키들을 포함하는지 검증Map이 지정한 키만 포함하는지 검증Map이 지정한 키들을 포함하지 않는지 검증 |
|  | containsValues(VALUE… values) | Map이 지정한 값들을 포함하는지 검증 |
|  | contains(Entry<K,V>… values) | Map이 지정한 Entry<K,V>를 포함하는지 검증 |

### 익셉션 관련 검증 메서드

- assertThatThrownBy() 메서드 인자로 받은 람다에서 익셉션이 발생하는지 검증한다
    - 발생한 익셉션의 타입을 추가로 검증하고 싶다면 isInstanceOf() 메서드
- assertThatExceptionOfType() 발생할 익셉션의 타입을 저장
    - isThrownBy() 메서드를 이용해서 익셉션이 발생할 코드 블록을 지정
- assertThatIoException() 메서드는 IOException이 발생하는 것을 검증
    - 유사) assertThatNullPointerException(), assertThatllegalArgumentException(), assertThatIllegalStateException()
- 익셉션이 발생하지 않는 것 검증

```java
assertThatCode(()->{

}).doesNotThrowAnyException
```

### as()와 describedAs()로 설명 달기

as() 메서드 대신에 describedAs() 메서드를 사용해도 됨

- as 메서드로 지정한 설명 문구

```java
assertThat(id).as("ID 검사").isEqualTo("abc");
```

- as 메서드에 문자열 포맷 사용

```java
// 첫번째 인자는 포맷팅을 포함한 문자열, 두번쨰 인자부터는 포맷팅에 사용할 값
asserThat(id).as("ID 검사: %s", "abc").isEqualTo("abc");
```

- 한 테스트 내 다수의 검증 메서드

```java
List<Integer> ret = gerResults();

List<Integer> expected = Arrays.asList(1,2,3);
SoftAssertions soft = new SoftAssertions();
for(int i=0; i<expected.size(); i++){
	soft.assertThat(req.get(i).as("ret[%d]",i).isEqualsTo(expected.get(i));
}
soft.assertAll;
```

