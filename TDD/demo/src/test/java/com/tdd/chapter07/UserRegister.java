package com.tdd.chapter07;

public class UserRegister {
    private WeakPasswordChecker passwordChecker;
    private UserRepository userRepository;

    public UserRegister(WeakPasswordChecker passwordChecker, MemoryUserRepository userRegister) {
        this.passwordChecker = passwordChecker;
        this.userRepository = userRegister;
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
    }
}
