package com.tdd.chap07;

import java.time.LocalDateTime;

public class AutoDebitRegister {
    private CardNumberValidator validar;
    private AutoDebitInfoRepository repository;

    public AutoDebitRegister(CardNumberValidator valider,
                             AutoDevitRepository repository){
        this.validar = valider;
        this.repository = repository;
    }
    public RegisterResult register(AutoDebitRegister req){
        CardValidity validity = validar.validate(req.getCardNumber());

        if(validity != CardValidity.VALID){
            return RegisterResult.error(validity);
        }
        AutoDebitInfo info = repository.findOne(req.getUserId());
        if(info != null){
            info.changeCardNumber(req.getCardNumber());
        }else{
            AutoDebitInfo newInfo =
                    new AutoDebitInfo(req.getUSerId(), req.getCardNumber(), LocalDateTime.now());
            repository.save(newInfo);
        }
        return RefisterResult.success();
    }
}
