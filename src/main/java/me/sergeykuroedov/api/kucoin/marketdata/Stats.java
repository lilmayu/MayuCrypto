package me.sergeykuroedov.api.kucoin.marketdata;

/*
    GET /api/v1/market/stats

    GET /api/v1/market/stats?symbol=ETH-BTC:
    {
    "symbol": "ETH-BTC",    // symbol
    "high": "0.03736329",   // 24h highest price
    "vol": "2127.286930263025",  // 24h volume，the aggregated trading volume in ETH
    "volValue": "43.58567564",  // 24h total, the trading volume in quote currency of last 24 hours
    "last": "0.03713983",   // last price
    "low": "0.03651252",    // 24h lowest price
    "buy": "0.03712118",    // bestAsk
    "sell": "0.03713983",   // bestBid
    "changePrice": "0.00037224",  // 24h change price
    "averagePrice": "8699.24180977",//24h average transaction price yesterday
    "time": 1550847784668,  //time
    "changeRate": "0.0101" // 24h change rate
    }
 */

import me.sergeykuroedov.api.kucoin.Kucoin;
import me.sergeykuroedov.utils.Request;
import org.json.JSONObject;

import java.io.IOException;

public class Stats {
    String symbol;
    Stats(String symbol) {
        this.symbol = symbol;
    }

    public JSONObject get() throws IOException {
        String json = Request.get(Kucoin.apiUrl+"/api/v1/market/stats?symbol="+symbol);
        return new JSONObject(json);
    }
}
