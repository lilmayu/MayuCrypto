package me.sergeykuroedov.murkoin;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import me.sergeykuroedov.murkoin.commands.Price;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class MurKoin {
    static public Config config = new Config("config.yml");

    public static void main(String args[]) throws LoginException, InterruptedException {
        CommandClientBuilder client = new CommandClientBuilder()
                .useDefaultGame()
                .useHelpBuilder(false)
                .setOwnerId("296338968763432960")
                .setActivity(Activity.listening("Murka"))
                .setPrefix(config.getPrefix())
                .addCommand(new Price());

        JDA api = JDABuilder.createDefault(config.getDiscordToken())
                .addEventListeners(client.build())
                .build()
                .awaitReady();
    }
}
