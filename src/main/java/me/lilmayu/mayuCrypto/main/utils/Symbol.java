package me.lilmayu.mayuCrypto.main.utils;

/**
 * Code from MurKoin
 */

public class Symbol {

    private String first;
    private String second;

    public Symbol(String str) {
        str = str.toUpperCase();
        String[] arr = str.split("-");
        if (arr.length == 1) {
            arr = str.split(" ");
            if (arr.length == 2) {
                first = arr[0];
                second = arr[1];
            } else {
                first = arr[0];
                second = "USDT";
            }
        } else {
            first = arr[0];
            second = arr[1];
        }
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String str) {
        first = str;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String str) {
        second = str;
    }

    @Override
    public String toString() {
        return first + "-" + second;
    }
}
