package com.tdd.chapter07;

public class SpyEmailNotifier implements EmailNotifier{
    private boolean called;
    private String email;

    public boolean isCalled() {
        return called;
    }

    public String getEmail() {
        return email;
    }

    // 스파이의 이메일 발송 기능 구현에서 호출 여부 기록
    @Override
    public void sendRegisterEmail(String email) {
        this.called = true;
        this.email = email;
    }
}
