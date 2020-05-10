package me.sergeykuroedov.utils;

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

    public void setFirst(String str) {
        first = str;
    }

    public String getFirst() {
        return first;
    }

    public void setSecond(String str) {
        second = str;
    }

    public String getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return first + "-" + second;
    }
}
