package com.yunbao.common.utils;

import java.math.BigDecimal;

public class CalculateUtil {

    public static BigDecimal multiply1(String num1, String num2) {
        BigDecimal bigDecimal = new BigDecimal(num1);
        BigDecimal bigDecima2 = new BigDecimal(num2);
        return bigDecimal.multiply(bigDecima2);
    }

    public static double multiply2(String num1, String num2) {
        BigDecimal bigDecimal = new BigDecimal(num1);
        BigDecimal bigDecima2 = new BigDecimal(num2);
        return bigDecimal.multiply(bigDecima2).doubleValue();
    }
}
