package me.sergeykuroedov.murkoin.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.sergeykuroedov.api.kucoin.Kucoin;
import me.sergeykuroedov.utils.Symbol;
import org.json.JSONObject;

public class Price extends Command {

    public Price() {
        this.name = "Price";
        this.aliases = new String[]{"price"};
    }

    @Override
    protected void execute(CommandEvent event) {
        Symbol symbol = new Symbol(event.getArgs());
        try {
            JSONObject ticker = Kucoin.marketData.getTicker(symbol.toString());
            String code = ticker.getString("code");
            if(code.equals("200000")) {
                event.reply("Price for **" + symbol.getFirst() + "** is: **" + ticker.getJSONObject("data").getString("price") + " " + symbol.getSecond() + "**");
            } else {
                event.reply(ticker.getString("msg"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
