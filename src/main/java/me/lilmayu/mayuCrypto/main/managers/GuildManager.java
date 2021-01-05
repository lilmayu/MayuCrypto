package me.lilmayu.mayuCrypto.main.managers;

import lombok.Getter;
import me.lilmayu.mayuCrypto.main.Main;
import me.lilmayu.mayuCrypto.main.objects.MayuGuild;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class GuildManager {

    private @Getter List<MayuGuild> mayuGuildList = new ArrayList<>();

    public GuildManager() {
    }

    public void refreshGuilds() {
        Thread thread = new Thread(() -> {
            Thread.currentThread().setName("RefreshGuildsThread");
            long start = System.currentTimeMillis();
            Logger.info("Refreshing guilds...");

            List<Guild> guildList = new ArrayList<>(Main.getJDAApi().getGuilds());
            Logger.debug(" -> Bot is on " + guildList.size() + " guilds.");



            Logger.success("Successfully refreshed guilds, took " + (System.currentTimeMillis() - start) + "ms!");
        });
        thread.start();
    }

    public MayuGuild getGuild(String guildID) {
        for (MayuGuild mayuGuild : mayuGuildList) {
            if (mayuGuild.getGuild().getId().equals(guildID))
                return mayuGuild;
        }
        return null;
    }

    public void updateGuildList(MayuGuild mayuGuild) {
        Thread thread = new Thread(() -> {
            Thread.currentThread().setName("UpdateGuildListThread");
            int counter = 0;
            for (MayuGuild mayuGuildInList : mayuGuildList) {
                if (mayuGuildInList.getGuild().getIdLong() == mayuGuild.getGuild().getIdLong()) {
                    mayuGuildList.remove(counter);
                    mayuGuildList.add(mayuGuild);
                }
                counter++;
            }
        });
        thread.start();
    }
}
