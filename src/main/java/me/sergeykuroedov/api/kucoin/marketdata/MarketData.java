package me.sergeykuroedov.api.kucoin.marketdata;

import me.sergeykuroedov.utils.Symbol;
import org.json.JSONObject;

import java.io.IOException;

public class MarketData {
    public JSONObject getTicker(String symbol) throws IOException {
        return new Ticker(symbol).get();
    }

    public JSONObject getStats(String symbol) throws IOException {
        return new Stats(symbol).get();
    }

    public JSONObject getKlines(Symbol symbol, long start, long end, String type) throws IOException {
        return new Klines(symbol, start, end, type).get();
    }
}
