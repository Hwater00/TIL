package com.tdd.chap07;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class AutoDebitRegister_Fake_Test {
    private AutoDebitRegister register;
    private StubCardNumberValidator cardNumberValidator;
    private MemoryAutoDebitInfoRepository repository;

    @BeforeEach
    void setUp(){
        // 카드 정보 API를 대신해서 유효한 카드번호, 도난 카드번호와 같은 상황을 흉내 낸다
        cardNumberValidator = new StubCardNumberValidator();
        // 특정 사용자에 대한 자동이체 정보가 이미 등록되어 있거나 등록되어 있지 않은 상황을 흉내낸다
        repository = new MemoryAutoDebitInfoRepository();
        register = new AutoDebitRegister(cardNumberValidator,repository);
    }

    @Test
    void alreadyRegistered_InfoUpdated(){
        repository.save(new AutoDebitInfo("user1","111222333444", LocalDate.now()));
        AutoDebitInfo saved = repository.findOne("user1");
        assertEquals("123412341234",saved.getCardNumber());
    }

    @Test
    void notYetRegistered_newInfoRegistered(){
        AutoDebitReq req = new AutoDebitReq("user1","123412341234");
        RegisterResult result = this.register.register(req);

        AutoDebitInfo saved = repository.findOne("user1");
        assertEquals("123412341234",saved.getCardNumber());
    }
}
