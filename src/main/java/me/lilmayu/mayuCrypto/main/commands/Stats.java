package me.lilmayu.mayuCrypto.main.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.lilmayu.mayuCrypto.api.kucoin.Kucoin;
import me.lilmayu.mayuCrypto.main.utils.Chart;
import me.lilmayu.mayuCrypto.main.utils.Image;
import me.lilmayu.mayuCrypto.main.utils.Symbol;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
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
        MessageChannel messageChannel = event.getChannel();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Generating stats graph...");
        Message message = messageChannel.sendMessage(embedBuilder.build()).complete();
        Image finalImage = null;
        Exception exception = null;
        EmbedBuilder eb = null;
        try {
            JSONObject stats = Kucoin.marketData.getStats(symbol.toString());
            String code = stats.getString("code");
            if(code.equals("200000")) {
                JSONObject data = stats.getJSONObject("data");
                double percentage = data.getDouble("changeRate") * 100;
                eb = new EmbedBuilder()
                        .setTitle("Stats "+symbol)
                        .addField("Price", data.getString("last"), false)
                        .addField("24h high", data.getString("high"), true)
                        .addField("24h low", data.getString("low"), true)
                        .addField("Change", data.getString("changePrice") + " (" + Math.round(percentage  *100.0) / 100.0 + " %)", true);
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
                    finalImage = new Image(f).convertToPng("graph.png");
                    eb.setImage("attachment://graph.png");
                    message.delete().complete();
                    //event.getChannel().sendFile(finalImage.getFile(), "graph.png").embed(eb.build()).queue();
                } else {
                    //event.reply(eb.build());
                }
            } else {
                //event.reply(stats.getString("msg"));
            }
            //message.delete().complete();
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
            Logger.error("Error occurred while creating a stats!");
        }

        if (finalImage == null) {
            EmbedBuilder errorMessageEmbed = new EmbedBuilder();
            errorMessageEmbed.setTitle("Error occurred!");
            if (exception != null) {
                StringBuilder stackTrace = new StringBuilder("```\n");
                StackTraceElement[] stackTraceElements = exception.getStackTrace();
                for (StackTraceElement stackTraceElement : stackTraceElements) {
                    if (stackTrace.toString().length() + stackTraceElement.toString().length() >= 1024) {
                        if (stackTrace.toString().length() <= 1020) {
                            stackTrace.append("...");
                        }
                        break;
                    }
                    stackTrace.append(stackTraceElement.toString()).append("\n");
                }
                stackTrace.append("```");
                errorMessageEmbed.addField("***Full stack trace***", stackTrace.toString(), false);
                Logger.debug("here 1");
            } else {
                errorMessageEmbed.addField("***Full stack trace***", "There was not any exception. Image object is null.", false);
                Logger.debug("here 2");
            }
            message.editMessage(errorMessageEmbed.build()).queue();
            Logger.debug("here 3");
        } else {
            event.getChannel().sendFile(finalImage.getFile(), "graph.png").embed(eb.build()).queue();
        }
    }
}
