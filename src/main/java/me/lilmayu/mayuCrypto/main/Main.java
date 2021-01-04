package me.lilmayu.mayuCrypto.main;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import lombok.Getter;
import me.lilmayu.mayuCrypto.main.commands.Price;
import me.lilmayu.mayuCrypto.main.commands.Stats;
import me.lilmayu.mayuCrypto.main.configUtils.BotConfig;
import me.lilmayu.mayuCrypto.main.managers.ChartManager;
import me.lilmayu.mayuCrypto.main.utils.ExceptionInformer;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {

    // Config
    public static @Getter BotConfig botConfig;

    // JDA Api
    private static @Getter JDA JDAApi;

    // Managers
    private static @Getter ChartManager chartManager;

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

        Logger.info("Loading managers...");
        chartManager = new ChartManager();

        CommandClientBuilder client = new CommandClientBuilder()
                .useDefaultGame()
                .useHelpBuilder(false)
                .setOwnerId("680508886574170122")
                .setActivity(Activity.playing("Built on MurKoin!"))
                .setPrefix(botConfig.getPrefix())
                .addCommand(new Price())
                .addCommand(new Stats());

        JDAApi = JDABuilder.createDefault(botConfig.getDiscordToken())
                .addEventListeners(client.build())
                .build()
                .awaitReady();

        Logger.success("Loading done, took " + (System.currentTimeMillis() - startStartup) + "ms!");
    }
}
