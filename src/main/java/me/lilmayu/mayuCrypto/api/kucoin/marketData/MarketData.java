package me.lilmayu.mayuCrypto.api.kucoin.marketData;

import me.lilmayu.mayuCrypto.main.objects.KlinesType;
import me.lilmayu.mayuCrypto.main.utils.Symbol;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Code from MurKoin, edited by lilmayu
 */

public class MarketData {

    public JSONObject getTicker(String symbol) throws IOException {
        return new Ticker(symbol).get();
    }

    public JSONObject getStats(String symbol) throws IOException {
        return new Stats(symbol).get();
    }

    public JSONObject getKlines(Symbol symbol, long start, long end, String type) throws IOException {
        return new Klines(symbol, start, end, KlinesType.getByType(type)).get();
    }

    public JSONObject getKlines(Symbol symbol, long start, long end, KlinesType type) throws IOException {
        return new Klines(symbol, start, end, type).get();
    }
}
