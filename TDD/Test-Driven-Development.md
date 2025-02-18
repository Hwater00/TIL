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


