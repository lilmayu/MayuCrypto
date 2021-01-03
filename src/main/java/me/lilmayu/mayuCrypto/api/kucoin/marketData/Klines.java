package me.lilmayu.mayuCrypto.api.kucoin.marketData;

import me.lilmayu.mayuCrypto.api.kucoin.Kucoin;
import me.lilmayu.mayuCrypto.main.utils.Request;
import me.lilmayu.mayuCrypto.main.utils.Symbol;
import org.json.JSONObject;

import java.io.IOException;

/*
    GET /api/v1/market/candles


    GET /api/v1/market/candles?type=1min&symbol=BTC-USDT&startAt=1566703297&endAt=1566789757:

    [
        [
            "1545904980",             //Start time of the candle cycle
            "0.058",                  //opening price
            "0.049",                  //closing price
            "0.058",                  //highest price
            "0.049",                  //lowest price
            "0.018",                  //Transaction amount
            "0.000945"                //Transaction volume
        ],
        [
            "1545904920",
            "0.058",
            "0.072",
            "0.072",
            "0.058",
            "0.103",
            "0.006986"
        ]
    ]
 */

/**
 * Code from MurKoin
 */

public class Klines {

    private Symbol symbol;
    private long start;
    private long end;
    private String type;

    Klines(Symbol symbol, long start, long end, String type) {
        this.symbol = symbol;
        this.start = start;
        this.end = end;
        this.type = type;
    }

    public JSONObject get() throws IOException {
        String url = Kucoin.apiUrl + String.format("/api/v1/market/candles?symbol=%s&startAt=%d&endAt=%d&type=%s", symbol, start, end, type);
        String json = Request.get(url);
        return new JSONObject(json);
    }
}