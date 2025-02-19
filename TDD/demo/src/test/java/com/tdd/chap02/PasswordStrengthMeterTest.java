package com.tdd.chap02;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class PasswordStrengthMeterTest {
    private PasswordStrengthMeter meter = new PasswordStrengthMeter();
    private void assertStrength(String password, PasswordStrength expStr){
        PasswordStrength result = meter.meter(password);
        assertEquals(expStr, result);
    }
    @Test
    // 첫 번쨰 테스트: 모든 규칙을 충족하는 경우
    void meetsAllCriteria_Then_Strong(){
        assertStrength("ab12!@AB",PasswordStrength.STRONG);
        assertStrength("abc1Add",PasswordStrength.STRONG);
    }

    @Test
    // 두 번째 테스트: 길이만 8글자 미만이고 나머지 조건은 충족하는 경우
    void meetsOtherCriteria_except_for_Length_Then_Normal(){
        assertStrength("ab12!@A",PasswordStrength.NORMAL);
    }

    @Test
    // 세 번째 테스트: 숫자를 포함하지 않고 나머지 조건은 충족하는 경우
    void meetsOtherCriteria_except_for_number_Then_Normal(){
        PasswordStrength result = meter.meter("ab!@ABqwer");
        assertEquals(PasswordStrength.NORMAL,result);
    }
}
