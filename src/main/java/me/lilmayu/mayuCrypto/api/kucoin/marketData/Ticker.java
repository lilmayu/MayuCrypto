package me.lilmayu.mayuCrypto.api.kucoin.marketData;

import me.lilmayu.mayuCrypto.api.kucoin.Kucoin;
import me.lilmayu.mayuCrypto.main.utils.Request;
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
        return new JSONObject(json);
    }
}