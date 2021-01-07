package me.lilmayu.mayuCrypto.main;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import lombok.Getter;
import me.lilmayu.mayuCrypto.main.commands.Help;
import me.lilmayu.mayuCrypto.main.commands.LiveStats;
import me.lilmayu.mayuCrypto.main.commands.Price;
import me.lilmayu.mayuCrypto.main.commands.Stats;
import me.lilmayu.mayuCrypto.main.configUtils.BotConfig;
import me.lilmayu.mayuCrypto.main.listeners.GuildListeners;
import me.lilmayu.mayuCrypto.main.managers.ChartManager;
import me.lilmayu.mayuCrypto.main.managers.GuildManager;
import me.lilmayu.mayuCrypto.main.managers.LiveStatsManager;
import me.lilmayu.mayuCrypto.main.utils.ExceptionInformer;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Main {

    // Config
    public static @Getter BotConfig botConfig;

    // JDA Api
    private static @Getter JDA JDAApi;

    // Managers
    private static @Getter ChartManager chartManager;
    private static @Getter GuildManager guildManager;
    private static @Getter LiveStatsManager liveStatsManager;

    public static void main(String[] args) throws LoginException, InterruptedException {
        long startStartup = System.currentTimeMillis();
        Logger.info("MayuCrypto -> Hello!");

        Logger.info("Loading main Config file...");
        botConfig = new BotConfig("config.json");
        if (!botConfig.isValid()) {
            Logger.error("Config isn't valid, please check config.json!");
            return;
        }

        Logger.info("Registering ExceptionInformer...");
        ExceptionInformer.registerExceptionHandler();

        Logger.info("Registering Runtime Shutdown Hook...");
        Runtime.getRuntime().addShutdownHook(getShutdownHookThread());

        Logger.info("Loading managers...");
        chartManager = new ChartManager();
        guildManager = new GuildManager();
        liveStatsManager = new LiveStatsManager();

        CommandClientBuilder client = new CommandClientBuilder()
                .useDefaultGame()
                .useHelpBuilder(false)
                .setOwnerId("680508886574170122")
                .setActivity(Activity.playing("Built on MurKoin!"))
                .setPrefix(botConfig.getPrefix())
                .addCommand(new Price())
                .addCommand(new Stats())
                .addCommand(new Help())
                .addCommand(new LiveStats());

        Logger.info("Registering a JDA...");

        JDAApi = JDABuilder.createDefault(botConfig.getDiscordToken())
                .addEventListeners(client.build())
                .enableIntents(GatewayIntent.GUILD_PRESENCES)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.DIRECT_MESSAGES)
                .enableIntents(GatewayIntent.DIRECT_MESSAGE_REACTIONS)
                .addEventListeners(new GuildListeners())
                .build()
                .awaitReady();

        guildManager.refreshGuilds();
        liveStatsManager.startTimer();

        Logger.success("Loading done, took " + (System.currentTimeMillis() - startStartup) + "ms!");
    }

    private static Thread getShutdownHookThread() {
        Thread thread = new Thread(() -> {
            Logger.info("Shutting down MayuCrypto...");
            Main.getGuildManager().saveGuildDatabase(true, true);
            Logger.success("Successfully saved guild database!");

            Logger.info("Bye.~");
        });
        return thread;
    }
}
