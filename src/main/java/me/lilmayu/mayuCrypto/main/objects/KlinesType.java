package me.lilmayu.mayuCrypto.main.objects;

import lombok.Getter;

public enum KlinesType {
    MIN_1("1min", 60),
    MIN_3("3min", 3 * 60),
    MIN_15("15min", 15 * 60),
    MIN_30("30min", 30 * 60),
    HOUR_1("1hour", 60 * 60),
    HOUR_2("2hour", 2 * 60 * 60),
    HOUR_4("4hour", 4 * 60 * 60),
    HOUR_6("6hour", 6 * 60 * 60),
    HOUR_8("8hour", 8 * 60 * 60),
    HOUR_12("12hour", 12 * 60 * 60),
    DAY("1day", 24 * 60 * 60),
    WEEK("1week", 7 * 24 * 60 * 60);

    private @Getter final String type;
    private @Getter final long s;

    KlinesType(String type, long s) {
        this.type = type;
        this.s = s;
    }

    public static KlinesType getByType(String type) {
        switch (type) {
            case "1min":
            case "1m":
                return KlinesType.MIN_1;
            case "3min":
            case "3m":
                return KlinesType.MIN_3;
            case "15min":
            case "15m":
                return KlinesType.MIN_15;
            case "30min":
            case "30m":
                return KlinesType.MIN_30;
            case "2hour":
            case "2h":
                return KlinesType.HOUR_2;
            case "4hour":
            case "4h":
                return KlinesType.HOUR_4;
            case "6hour":
            case "6h":
                return KlinesType.HOUR_6;
            case "8hour":
            case "8h":
                return KlinesType.HOUR_8;
            case "12hour":
            case "12h":
                return KlinesType.HOUR_12;
            case "1day":
            case "day":
            case "d":
                return KlinesType.DAY;
            case "1week":
            case "week":
            case "w":
                return KlinesType.WEEK;
            default:
                return KlinesType.HOUR_1;
        }
    }
}
