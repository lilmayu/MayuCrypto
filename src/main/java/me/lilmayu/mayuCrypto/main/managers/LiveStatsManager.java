package me.lilmayu.mayuCrypto.main.managers;

import me.lilmayu.mayuCrypto.api.kucoin.Kucoin;
import me.lilmayu.mayuCrypto.main.Main;
import me.lilmayu.mayuCrypto.main.objects.ChartFile;
import me.lilmayu.mayuCrypto.main.objects.guilds.ManagedMessage;
import me.lilmayu.mayuCrypto.main.objects.guilds.MayuGuild;
import me.lilmayu.mayuCrypto.main.objects.guilds.managed.ManagedMessageType;
import me.lilmayu.mayuCrypto.main.utils.CryptoSymbol;
import me.lilmayu.mayuCrypto.main.utils.ExceptionInformer;
import me.lilmayu.mayuCrypto.main.utils.Image;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.print.DocFlavor;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

public class LiveStatsManager {

    private Timer timer;

    public LiveStatsManager() {

    }

    public void startTimer() {
        Logger.info("Starting up Live-Stats timer...");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!Main.getGuildManager().isGuildsLoaded())
                    return;
                Logger.debug("Updating LiveStats!");
                for (MayuGuild mayuGuild : Main.getGuildManager().getMayuGuildList()) {
                    for (ManagedMessage managedMessage : mayuGuild.getManagedMessageList()) {
                        Logger.debug("Updated one live stat!");
                        updateLiveStats(managedMessage);
                    }
                }
            }
        }, 5000, 30000);
    }

    public static void updateLiveStats(ManagedMessage managedMessage) {
        if (managedMessage.getType() == ManagedMessageType.LIVE_CHART) {
            managedMessage.resolveMessageFromID();
            Message message = managedMessage.getMessage();
            CryptoSymbol symbol = managedMessage.getDataCryptoSymbol();

            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("Live Stats : " + symbol.toString());
            embedBuilder.setFooter("Powered by MayuCrypto");
            embedBuilder.setTimestamp(Instant.now());
            embedBuilder.setDescription("Update every 30 seconds");
            try {
                JSONObject stats = Kucoin.marketData.getStats(symbol.toString());
                String code = stats.getString("code");
                if (code.equals("200000")) {
                    JSONObject data = stats.getJSONObject("data");
                    double percentage = data.getDouble("changeRate") * 100;
                    embedBuilder.addField("Price", data.getString("last"), false);
                    embedBuilder.addField("24h high", data.getString("high"), true);
                    embedBuilder.addField("24h low", data.getString("low"), true);
                    embedBuilder.addField("24h change", data.getString("changePrice") + " (" + Math.round(percentage * 100.0) / 100.0 + " %)", true);
                    long epoch = System.currentTimeMillis() / 1000;
                    long start = epoch - ((managedMessage.getKlinesType().getS() * 24));
                    JSONObject klines = Kucoin.marketData.getKlines(symbol, start, epoch, managedMessage.getKlinesType());
                    if (klines.getString("code").equals("200000")) {
                        JSONArray arr = klines.getJSONArray("data");
                        ChartFile chartFile = Main.getChartManager().createNewChart(arr, 600, 180);
                        embedBuilder.setImage(chartFile.getURLToChart());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.error("Error while refreshing live-stats!");
                ExceptionInformer.handle(e);
            }

            message.editMessage(embedBuilder.build()).queue();
        }
    }
}
