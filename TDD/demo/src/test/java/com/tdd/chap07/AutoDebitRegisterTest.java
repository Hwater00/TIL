package com.tdd.chap07;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.tdd.chap07.CardValidity.THEFT;
import static com.tdd.chap07.CardValidity.VALID;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class AutoDebitRegisterTest {
    private AutoDebitRegister register;

    @BeforeEach
    void setUp(){
        CardNumberValidator validator = new CardNumberValidator();
        AutoDebitInfoRepository repository = new JpaAutoDebitInfoRepository();
        register = new AutoDebitRegister(validator,repository);
    }

    @Test
    void validCard(){
        // 업체에서 받은 테스트용 유효한 카드번호 사용
        AutoDebitReq req  = new AutoDebitReq("user1","1234123412341234");
        RegisterResult result = this.register.register(req);
        assertEquals(VALID, result.getValidity());
    }

    @Test
    void theftCard(){
        // 업체에서 받은 도난 테스트용 카드번호 사용
        AutoDebitReq req  = new AutoDebitReq("user1","1234567890123456");
        RegisterResult result = this.register.register(req);
        assertEquals(THEFT,result.getValidity());
    }
}
