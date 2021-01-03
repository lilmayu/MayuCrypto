package me.lilmayu.mayuCrypto.main.utils;

public class Utils {

    public static String currentMethod(int num) {
        return new Throwable()
                .getStackTrace()[num]
                .getMethodName();
    }
}
