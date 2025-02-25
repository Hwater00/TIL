package com.tdd.chapter07;

public class UserRegister {
    private WeakPasswordChecker passwordChecker;
    private UserRepository userRepository;
    private EmailNotifier emailNotifier;

    public UserRegister(WeakPasswordChecker passwordChecker, MemoryUserRepository userRegister, EmailNotifier emailNotifier) {
        this.passwordChecker = passwordChecker;
        this.userRepository = userRegister;
        this.emailNotifier =emailNotifier;
    }

    public void register(String id, String pw, String email){
        if(passwordChecker.checkPasswordWeak(pw)){
            throw new WeakPasswordException();
        }
        User user= userRepository.findById(id);
        if(user != null) {
            throw new DupIdException();
        }
        userRepository.save(new User(id,pw,email));

        // UserRegister가 EmailNotifier의 이메일 발송 기능을 호출
        emailNotifier.sendRegisterEmail(email);
    }
}
