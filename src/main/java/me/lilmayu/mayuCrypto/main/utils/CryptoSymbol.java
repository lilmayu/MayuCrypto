package me.lilmayu.mayuCrypto.main.utils;

import me.lilmayu.mayuCrypto.api.kucoin.Kucoin;
import org.json.JSONObject;

/**
 * Code from MurKoin
 */

public class CryptoSymbol {

    private String first;
    private String second;

    public CryptoSymbol(String str) {
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

    public boolean isValid() {
        try {
            JSONObject ticker = Kucoin.marketData.getTicker(toString());
            String code = ticker.getString("code");
            if (code.equals("200000")) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public String toString() {
        return first + "-" + second;
    }
}
