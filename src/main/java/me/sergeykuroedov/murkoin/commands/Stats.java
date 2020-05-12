package me.sergeykuroedov.murkoin.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.sergeykuroedov.api.kucoin.Kucoin;
import me.sergeykuroedov.utils.Chart;
import me.sergeykuroedov.utils.Image;
import me.sergeykuroedov.utils.Symbol;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;

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
                long epoch = System.currentTimeMillis()/1000;
                long start = epoch - 60*60*24;
                JSONObject klines = Kucoin.marketData.getKlines(symbol, start, epoch, "1hour");
                if(klines.getString("code").equals("200000")) {
                    JSONArray arr = klines.getJSONArray("data");
                    Chart ch = new Chart(arr, 600, 180);
                    File f = new File("temp.svg");
                    FileWriter fw = new FileWriter(f);
                    fw.write(ch.generate());
                    fw.close();
                    Image image = new Image(f).convertTransparent("graph.png");
                    eb.setImage("attachment://graph.png");
                    event.getChannel().sendFile(image.getFile(), "graph.png").embed(eb.build()).queue();
                } else {
                    event.reply(eb.build());
                }
            } else {
                event.reply(stats.getString("msg"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
