package com.tdd.chap03;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ExpiryDateCalculatorTest {
    @Test
    void 만원_납부하면_한달_뒤가_만료일이_됨(){
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,3,1))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019,4,1)
        );
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,5,5))
                        .payAmount(10_000)
                .build(),
                LocalDate.of(2019,6,5)
        );
    }

    @Test
    void 납부일과_한달_뒤가_일자가_같지_않음(){
        assertExpiryDate(
                PayData.builder()
                        .firstBillingDate(LocalDate.of(2019,1,31))
                        .billingDate(LocalDate.of(2019,1,31))
                        .payAmount(10_000)
                .build(),
                LocalDate.of(2019,2,28)
        );
    }

    @Test
    void 첫_납부일과_만료일_일자가_다를때_만원_닙부(){
                PayData payData3 =PayData.builder()
                        .firstBillingDate(LocalDate.of(2019,5,31))
                        .billingDate(LocalDate.of(2019,6,30))
                        .payAmount(10_000)
                        .build();
                assertExpiryDate(payData3,LocalDate.of(2019,7,31));
    }

    private void assertExpiryDate(
            PayData payData, LocalDate expectedExpiryDate){
        ExpiryDateCalculator cal = new ExpiryDateCalculator();
        LocalDate realExpiryDate = cal.calculateExpiryDate(payData);
        assertEquals(expectedExpiryDate, realExpiryDate);
    }

}
