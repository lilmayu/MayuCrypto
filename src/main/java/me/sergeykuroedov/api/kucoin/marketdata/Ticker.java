package me.sergeykuroedov.api.kucoin.marketdata;

import me.sergeykuroedov.utils.Request;
import me.sergeykuroedov.api.kucoin.Kucoin;
import org.json.JSONObject;

import java.io.IOException;

/*
    GET /api/v1/market/orderbook/level1


    GET /api/v1/market/orderbook/level1?symbol=BTC-USDT:

    "sequence": "1550467636704",
    "bestAsk": "0.03715004",
    "size": "0.17",
    "price": "0.03715005",
    "bestBidSize": "3.803",
    "bestBid": "0.03710768",
    "bestAskSize": "1.788",
    "time": 1550653727731
 */

public class Ticker {
    String symbol;
    Ticker(String symbol) {
        this.symbol = symbol;
    }

    public JSONObject get() throws IOException {
        String json = Request.get(Kucoin.apiUrl+"/api/v1/market/orderbook/level1?symbol="+symbol);
        System.out.println(json);
        return new JSONObject(json);
    }
}