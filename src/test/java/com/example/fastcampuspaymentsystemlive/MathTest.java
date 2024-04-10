package com.example.fastcampuspaymentsystemlive;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MathTest {
    @DisplayName("테스트")
    @Test
    public void add() {
        int a = 1;
        int b = 2;
        int sum = a + b;
        System.out.println(sum);
        Assertions.assertEquals(3, sum);
    }
}
