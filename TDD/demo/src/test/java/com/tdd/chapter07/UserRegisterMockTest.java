package com.tdd.chapter07;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserRegisterMockTest {
    private UserRegister userRegister;

    //Mockito.mock(인자)로 모의 객체 생성
    private WeakPasswordChecker mockPasswordChecker = Mockito.mock(WeakPasswordChecker.class);
    private MemoryUserRepository fakeRepository = new MemoryUserRepository();
    private EmailNotifier mockEmailNotifier = Mockito.mock(EmailNotifier.class);

    @BeforeEach
    void setUp(){
        userRegister = new UserRegister(mockPasswordChecker,fakeRepository,mockEmailNotifier);
    }

    @DisplayName("약한 암호면 가입 실패")
    @Test
    void weakPassword(){
        BDDMockito.given(mockPasswordChecker.checkPasswordWeak("pw"))
                .willReturn(true);

        assertThrows(WeakPasswordException.class, ()->{
            userRegister.register("id","pw","email");
        });
    }

    @DisplayName("모의 객체가 기대한 대로 불렸는지 검증하는 코드")
    @Test
    void checkPassword(){
        userRegister.register("id","pw","email");

        BDDMockito.then(mockPasswordChecker)
                .should() // 특정 메서드가 호출됐는지 검증
                .checkPasswordWeak(BDDMockito.anyString());
    }

    @DisplayName("가입하면 메일을 전송함")
    @Test
    void whenRegisterThenSendMail(){
        userRegister.register("id","pw","email@email.com");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        BDDMockito.then(mockEmailNotifier)
                .should().sendRegisterEmail(captor.capture());

        String realEmail = captor.getValue();
        assertEquals("email@email.com", realEmail);
    }
}
