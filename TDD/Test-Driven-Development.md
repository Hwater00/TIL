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


# chap02
- JUint은  @Test 애노테이션을 분인 메서드를 테스트 메서드로 인긱한다. 테스트 메서드는 기능을 검증하는 코드를 담고 있는 메서드이다.
- assertEqulas() 메서드는 인자로 받은 두 값이 동일한지 비교한다. 이때 첫 번째 인자는 기대한 값이고 두 번째 인자는 실제 값이다. 비교한 결과 두 값이 동일하지 않으면 AssertionFailedError가 발생한다.
org.opentest4j.AssertionFailedError



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
