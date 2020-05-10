package me.sergeykuroedov.murkoin.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.sergeykuroedov.api.kucoin.Kucoin;
import me.sergeykuroedov.utils.Symbol;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONObject;

public class Stats extends Command {

    public Stats() {
        this.name = "Stats";
        this.aliases = new String[] {"stats"};
    }

    @Override
    protected void execute(CommandEvent event) {
        Symbol symbol = new Symbol(event.getArgs());
        try {
            JSONObject stats = Kucoin.marketData.getStats(symbol.toString());
            String code = stats.getString("code");
            if(code.equals("200000")) {
                JSONObject data = stats.getJSONObject("data");
                double percentage = data.getDouble("changeRate") * 100;
                EmbedBuilder eb = new EmbedBuilder()
                        .setTitle("Stats "+symbol)
                        .addField("Price", data.getString("last"), false)
                        .addField("24h high", data.getString("high"), false)
                        .addField("24h low", data.getString("low"), false)
                        .addField("Change", data.getString("changePrice") + " (" +percentage + " %)", false);
                event.reply(eb.build());
            } else {
                event.reply(stats.getString("msg"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
