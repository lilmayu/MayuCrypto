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
        String args = event.getArgs();
        try {
            JSONObject ticker = Kucoin.marketData.getTicker(args);
            String code = ticker.getString("code");
            if(code.equals("200000")) {
                event.reply(ticker.getJSONObject("data").getString("price"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
