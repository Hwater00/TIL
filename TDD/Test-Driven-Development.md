<h1>테스트 주도 개발 시작하기(최범균, 가메출판사)</h1>

<h2> chap02 </h2>
- JUint은  @Test 애노테이션을 분인 메서드를 테스트 메서드로 인긱한다. 테스트 메서드는 기능을 검증하는 코드를 담고 있는 메서드이다.
- assertEqulas() 메서드는 인자로 받은 두 값이 동일한지 비교한다. 이때 첫 번째 인자는 기대한 값이고 두 번째 인자는 실제 값이다. 비교한 결과 두 값이 동일하지 않으면 AssertionFailedError가 발생한다.
org.opentest4j.AssertionFailedError