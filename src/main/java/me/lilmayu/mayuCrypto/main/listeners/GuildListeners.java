package me.lilmayu.mayuCrypto.main.listeners;

import me.lilmayu.mayuCrypto.main.Main;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildListeners extends ListenerAdapter {

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent guildJoinEvent) {
        Logger.info("Someone added me in new guild! Adding guild to database...");
        Main.getGuildManager().createNewGuildInDatabase(guildJoinEvent.getGuild());
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent guildLeaveEvent) {
        String message = "Someone removed me from guild. :(";
        boolean remove = Main.getBotConfig().isRemoveGuildFromDatabaseOnLeave();
        if (remove) {
            message += " Removing guild from database...";
        }
        Logger.info(message);
        if (remove) Main.getGuildManager().removeGuildFromDatabase(guildLeaveEvent.getGuild().getId());
    }
}
