package me.sergeykuroedov.api.kucoin.marketdata;

import org.json.JSONObject;

import java.io.IOException;

public class MarketData {
    public JSONObject getTicker(String symbol) throws IOException {
        return new Ticker(symbol).get();
    }
}
