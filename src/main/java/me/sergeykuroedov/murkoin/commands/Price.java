package me.sergeykuroedov.murkoin.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.sergeykuroedov.api.kucoin.Kucoin;
import org.json.JSONObject;

public class Price extends Command {

    public Price() {
        this.aliases = new String[]{"price"};
    }

    @Override
    protected void execute(CommandEvent event) {
        String symbol = event.getArgs();
        symbol = symbol.toUpperCase();
        String[] arr = symbol.split("-");
        if(arr.length == 1) {
            arr = symbol.split(" ");
            if(arr.length == 2) {
                symbol = arr[0]+"-"+arr[1];
            } else {
                symbol = arr[0]+"-USDT";
                arr = symbol.split("-");
            }
        }

        try {
            JSONObject ticker = Kucoin.marketData.getTicker(symbol);
            String code = ticker.getString("code");
            if(code.equals("200000")) {
                event.reply("Price for **" + arr[0] + "** is: **" + ticker.getJSONObject("data").getString("price") + " " + arr[1] + "**");
            } else {
                event.reply(ticker.getString("msg"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
