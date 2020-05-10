package me.sergeykuroedov.api.kucoin.marketdata;

import com.google.gson.Gson;
import me.sergeykuroedov.utils.Request;
import me.sergeykuroedov.api.kucoin.Kucoin;

import java.io.IOException;
import java.util.Currency;

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
        symbol = symbol.toUpperCase();
        String[] arr = symbol.split("-");
        if(arr[1] == null) {
            this.symbol = arr[0]+"-USDT";
        }
    }

    public TickerEntity get() throws IOException {
        String json = Request.get(Kucoin.apiUrl+"/api/v1/market/orderbook/level1?symbol="+symbol);
        return new Gson().fromJson(json, TickerEntity.class);
    }

    public static class TickerEntity {
        int sequence;
        Currency bestAsk, size, price, bestBidSize, bestBid, bestAskSize;
        int time;
    }
}