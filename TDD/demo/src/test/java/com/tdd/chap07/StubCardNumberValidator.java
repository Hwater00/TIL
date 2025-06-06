package com.tdd.chap07;

public class StubCardNumberValidator extends CardNumberValidator{
    private String invalidNo;
    private String theftNo;

    public void setInvalidNo(String invalidNo){
        this.invalidNo = invalidNo;
    }

    public void setTheftNo(String theftNo){
        this.theftNo = theftNo;
    }

    @Override
    public CardValidity validity(String cardNumber){
        if(invalidNo != null && invalidNo.equals(cardNumber)){
            return CardValidity.INVALID;
        }
        if(theftNo != null && theftNo.equals(cardNumber)){
            return  CardValidity.THEFT;
        }
        return CardValidity.VALID;
    }
}
