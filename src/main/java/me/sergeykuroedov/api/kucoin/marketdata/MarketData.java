package me.sergeykuroedov.api.kucoin.marketdata;

import java.io.IOException;

public class MarketData {
    static Ticker.TickerEntity getTicker(String symbol) throws IOException {
        return new Ticker(symbol).get();
    }
}
