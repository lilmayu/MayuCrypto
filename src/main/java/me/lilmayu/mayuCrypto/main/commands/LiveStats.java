package me.lilmayu.mayuCrypto.main.commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import me.lilmayu.mayuCrypto.main.Main;
import me.lilmayu.mayuCrypto.main.managers.LiveStatsManager;
import me.lilmayu.mayuCrypto.main.objects.MayuCommand;
import me.lilmayu.mayuCrypto.main.objects.guilds.ManagedMessage;
import me.lilmayu.mayuCrypto.main.utils.CryptoSymbol;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public class LiveStats extends MayuCommand {

    public LiveStats() {
        this.name = "Live-stats";
        this.aliases = new String[]{"live-stats", "livestats"};

        Help.addCommand(this);
    }

    @Override
    public String getDescription() {
        return "This will create live-stats embed in current channel. Only users with administrator permissions can add / remove live-stats. Removing is done via reactions. Selection time is also done via reactions.";
    }

    @Override
    public String getSyntax() {
        return "[CryptoCurr. Symbol]";
    }

    @Override
    public List<String> getExamples() {
        List<String> examples = new ArrayList<>();

        examples.add("BTC-USDT");
        examples.add("ETH-USDT");

        return examples;
    }

    @Override
    protected void execute(CommandEvent event) {
        Member member = event.getMember();
        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("You don't have Administrator permission!");
            return;
        }
        String args = event.getArgs();
        if (args.length() >= 1) {
            CryptoSymbol symbol = new CryptoSymbol(args);
            if (!symbol.isValid()) {
                event.reply("Cryptocurrency symbol isn't valid!");
                return;
            }
            TextChannel textChannel = event.getTextChannel();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Preparing live-stats...");

            Message message = textChannel.sendMessage(embedBuilder.build()).complete();

            ManagedMessage managedMessage = ManagedMessage.createLiveStats(event.getGuild().getId(), message, textChannel, symbol);
            LiveStatsManager.updateLiveStats(managedMessage);
            event.getMessage().delete().queue();
        }
    }
}
