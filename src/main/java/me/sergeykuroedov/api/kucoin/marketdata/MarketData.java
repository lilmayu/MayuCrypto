package me.sergeykuroedov.api.kucoin.marketdata;

import org.json.JSONObject;

import java.io.IOException;

public class MarketData {
    public JSONObject getTicker(String symbol) throws IOException {
        return new Ticker(symbol).get();
    }

    public JSONObject getStats(String symbol) throws IOException {
        return new Stats(symbol).get();
    }
}
