package me.lilmayu.mayuCrypto.main.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.lilmayu.mayuCrypto.api.kucoin.Kucoin;
import me.lilmayu.mayuCrypto.main.Main;
import me.lilmayu.mayuCrypto.main.objects.ChartFile;
import me.lilmayu.mayuCrypto.main.objects.KlinesType;
import me.lilmayu.mayuCrypto.main.objects.MayuCommand;
import me.lilmayu.mayuCrypto.main.utils.ExceptionInformer;
import me.lilmayu.mayuCrypto.main.utils.Image;
import me.lilmayu.mayuCrypto.main.utils.Symbol;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Code from MurKoin, edited by lilmayu
 */

public class Stats extends MayuCommand {

    public Stats() {
        this.name = "Stats";
        this.aliases = new String[]{"stats"};
        this.guildOnly = false;

        Help.addCommand(this);
    }

    @Override
    protected void execute(CommandEvent event) {
        MessageChannel messageChannel = event.getChannel();
        EmbedBuilder embedBuilderGenerating = new EmbedBuilder();
        embedBuilderGenerating.setTitle("Generating stats graph...");
        Message message = messageChannel.sendMessage(embedBuilderGenerating.build()).complete();

        Symbol symbol;
        KlinesType klinesType = KlinesType.HOUR_1;

        String args = event.getArgs();
        String[] argsArray = args.split(" ");

        if (argsArray.length >= 1) {
            symbol = new Symbol(argsArray[0]);
            if (symbol.getFirst() == null || symbol.getSecond() == null) {
                message.delete().complete();
                event.reply("Invalid arguments! Please, see `!mc help stats`.");
                return;
            }
            if (argsArray.length == 2) {
                klinesType = KlinesType.getByType(argsArray[1]);
            }
        } else {
            message.delete().complete();
            event.reply("No arguments we're passed! Please, see `!mc help stats`.");
            return;
        }


        Image finalImage = null;
        Exception exception = null;
        EmbedBuilder embedBuilderWithGraph = null;

        try {
            JSONObject stats = Kucoin.marketData.getStats(symbol.toString());
            String code = stats.getString("code");
            if (code.equals("200000")) {
                JSONObject data = stats.getJSONObject("data");
                double percentage = data.getDouble("changeRate") * 100;
                embedBuilderWithGraph = new EmbedBuilder()
                        .setTitle("Stats " + symbol)
                        .addField("Price", data.getString("last"), false)
                        .addField("24h high", data.getString("high"), true)
                        .addField("24h low", data.getString("low"), true)
                        .addField("Change", data.getString("changePrice") + " (" + Math.round(percentage * 100.0) / 100.0 + " %)", true);
                long epoch = System.currentTimeMillis() / 1000;
                long start = epoch - ((klinesType.getS() * 24));
                JSONObject klines = Kucoin.marketData.getKlines(symbol, start, epoch, klinesType);
                if (klines.getString("code").equals("200000")) {
                    JSONArray arr = klines.getJSONArray("data");
                    ChartFile chartFile = Main.getChartManager().createNewChart(arr, 600, 180);
                    finalImage = chartFile.getImage();
                    embedBuilderWithGraph.setImage("attachment://" + finalImage.getFile().getName());
                    message.delete().complete();
                } else {
                    message.delete().complete();
                    event.reply(embedBuilderWithGraph.build());
                }
            } else {
                message.delete().complete();
                event.reply(stats.getString("msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionInformer.handle(e);
            exception = e;
            Logger.error("Error occurred while creating a stats!");
        }

        if (finalImage == null) {
            EmbedBuilder errorMessageEmbed = new EmbedBuilder();
            errorMessageEmbed.setTitle("Error occurred!");
            if (exception != null) {
                errorMessageEmbed.setDescription("Exception: " + exception.getMessage());
            } else {
                errorMessageEmbed.addField("***Full stack trace***", "There was not any exception. Image object is null.", false);
            }
            message.editMessage(errorMessageEmbed.build()).queue();
        } else {
            event.getTextChannel().sendFile(finalImage.getFile(), finalImage.getFile().getName()).embed(embedBuilderWithGraph.build()).queue();
        }
    }

    @Override
    public String getDescription() {
        return "Will send you graph for selected Cryptocurrency.";
    }

    @Override
    public String getSyntax() {
        return "[CryptoCurr. Symbol] <time>";
    }

    @Override
    public List<String> getExamples() {
        List<String> examples = new ArrayList<>();
        examples.add("BTC-USDT");
        examples.add("ETH-USDT 30min");
        examples.add("BTC-ETH 12hour");
        examples.add("BTC-USDT day");
        examples.add("BTC-USDT week");
        return examples;
    }
}
