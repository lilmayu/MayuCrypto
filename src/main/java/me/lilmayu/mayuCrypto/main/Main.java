package me.lilmayu.mayuCrypto.main;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import me.lilmayu.mayuCrypto.main.commands.Price;
import me.lilmayu.mayuCrypto.main.commands.Stats;
import me.lilmayu.mayuCrypto.main.configUtils.BotConfig;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {

    public static BotConfig botConfig;

    public static void main(String[] args) throws LoginException, InterruptedException {
        Logger.info("MayuCrypto -> Hello!");

        Logger.info("Loading main Config file...");
        botConfig = new BotConfig("config.json");
        if (!botConfig.isValid()) {
            Logger.error("Config isn't valid, please check config.json!");
            return;
        }

        CommandClientBuilder client = new CommandClientBuilder()
                .useDefaultGame()
                .useHelpBuilder(false)
                .setOwnerId("680508886574170122")
                .setActivity(Activity.playing("Built on MurKoin!"))
                .setPrefix(botConfig.getPrefix())
                .addCommand(new Price())
                .addCommand(new Stats());

        JDA api = JDABuilder.createDefault(botConfig.getDiscordToken())
                .addEventListeners(client.build())
                .build()
                .awaitReady();

        Logger.success("Loading done! Have a nice day.");
    }
}
