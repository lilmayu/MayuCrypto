package me.lilmayu.mayuCrypto.main.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.SneakyThrows;
import me.lilmayu.mayuCrypto.main.Main;
import me.lilmayu.mayuCrypto.main.configUtils.GuildConfig;
import me.lilmayu.mayuCrypto.main.objects.guilds.ManagedMessage;
import me.lilmayu.mayuCrypto.main.objects.guilds.ManagedTextChannel;
import me.lilmayu.mayuCrypto.main.objects.guilds.MayuGuild;
import me.lilmayu.mayuCrypto.main.objects.guilds.managed.ManagedMessageType;
import me.lilmayu.mayuCrypto.main.objects.guilds.managed.ManagedTextChannelType;
import me.lilmayu.mayuCrypto.main.utils.CryptoSymbol;
import me.lilmayu.mayuCrypto.main.utils.json.JsonUtils;
import me.lilmayu.mayuCrypto.main.utils.json.objects.JsonUtilObject;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class GuildManager {

    private @Getter List<MayuGuild> mayuGuildList = new ArrayList<>();

    private @Getter boolean guildsLoaded = false;

    public GuildManager() {
    }

    public void refreshGuilds() {
        Thread thread = new Thread(() -> {
            Thread.currentThread().setName("RefreshGuildsThread");
            long start = System.currentTimeMillis();
            Logger.info("Refreshing guilds...");
            guildsLoaded = false;
            mayuGuildList = new ArrayList<>();

            List<Guild> guildList = new ArrayList<>(Main.getJDAApi().getGuilds());
            JsonObject guildDatabaseJson = getGuildDatabaseJson();
            if (!guildDatabaseJson.has("guilds"))
                guildDatabaseJson.add("guilds", new JsonArray());
            JsonArray guildDatabaseArray = guildDatabaseJson.get("guilds").getAsJsonArray();
            Logger.debug(" -> Bot is on " + guildList.size() + " guilds.");

            for (JsonElement guildJsonDataElement : guildDatabaseArray) {
                List<ManagedMessage> managedMessagesList = new ArrayList<>();
                List<ManagedTextChannel> managedTextChannelList = new ArrayList<>();

                JsonObject guildJsonData = guildJsonDataElement.getAsJsonObject();

                String guildID = guildJsonData.get("guildID").getAsString();
                JsonArray managedMessages = guildJsonData.getAsJsonArray("managedMessages");
                JsonArray managedTextChannels = guildJsonData.getAsJsonArray("managedTextChannels");

                for (JsonElement managedMessageJsonDataElement : managedMessages) {
                    JsonObject managedMessageJsonData = managedMessageJsonDataElement.getAsJsonObject();

                    String messageID = managedMessageJsonData.get("messageID").getAsString();
                    String textChannelID = managedMessageJsonData.get("textChannelID").getAsString();
                    String type = managedMessageJsonData.get("type").getAsString();

                    JsonObject data = managedMessageJsonData.get("data").getAsJsonObject();

                    CryptoSymbol symbol = new CryptoSymbol(data.get("symbol").getAsString());

                    ManagedMessage managedMessage = new ManagedMessage(messageID, textChannelID, ManagedMessageType.fromString(type), symbol);
                    if (!managedMessage.isValidAtConstruct()) {
                        Logger.warning("See error above. ");
                        continue;
                    }
                    managedMessagesList.add(managedMessage);
                }

                for (JsonElement managedTextChannelJsonDataElement : managedTextChannels) {
                    JsonObject managedTextChannelJsonData = managedTextChannelJsonDataElement.getAsJsonObject();

                    String textChannelID = managedTextChannelJsonData.get("textChannelID").getAsString();
                    boolean isUser = managedTextChannelJsonData.get("isUser").getAsBoolean();
                    String userID = managedTextChannelJsonData.get("userID").getAsString();
                    String type = managedTextChannelJsonData.get("type").getAsString();

                    JsonObject data = managedTextChannelJsonData.get("data").getAsJsonObject();

                    String dataSymbol = data.get("symbol").getAsString();
                    int dataJumpValue = data.get("jump-value").getAsInt();

                    ManagedTextChannel managedTextChannel = new ManagedTextChannel(textChannelID, isUser, userID, ManagedTextChannelType.fromString(type), new CryptoSymbol(dataSymbol), dataJumpValue);
                    managedTextChannelList.add(managedTextChannel);
                }

                Guild guild = Main.getJDAApi().getGuildById(guildID);
                if (guild == null) {
                    Logger.error("Guild with ID '" + guildID + "' isn't connected with bot! Deleting from database...");
                    removeGuildFromDatabase(guildID);
                    continue;
                }

                MayuGuild mayuGuild = new MayuGuild(guild, new GuildConfig(), managedTextChannelList, managedMessagesList);
                mayuGuildList.add(mayuGuild);
            }

            Logger.success("Successfully loaded " + mayuGuildList.size() + " guilds from database. Now processing new ones, if there are some...");

            int counter = 0;
            for (Guild guild : guildList) {
                if (!isGuildInDatabase(guild.getId())) {
                    createNewGuildInDatabase(guild);
                    counter++;
                }
            }

            if (counter != 0)
                Logger.success("Successfully created " + counter + " new guilds in database!");
            else
                Logger.info("Guild database is up-to-date.");

            Logger.success("Successfully refreshed guilds, took " + (System.currentTimeMillis() - start) + "ms!");
            guildsLoaded = true;
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
            List<MayuGuild> mayuGuilds = new ArrayList<>(mayuGuildList);
            int counter = 0;
            for (MayuGuild mayuGuildInList : mayuGuilds) {
                if (mayuGuildInList.getGuild().getIdLong() == mayuGuild.getGuild().getIdLong()) {
                    mayuGuildList.remove(counter);
                    mayuGuildList.add(mayuGuild);
                }
                counter++;
            }
        });
        thread.start();
    }

    public boolean isGuildInDatabase(String guildID) {
        for (MayuGuild mayuGuild : mayuGuildList) {
            if (mayuGuild.getGuild().getId().equals(guildID)) {
                return true;
            }
        }
        return false;
    }

    public void createNewGuildInDatabase(Guild guild) {
        MayuGuild mayuGuild = new MayuGuild(guild, new GuildConfig(), new ArrayList<>(), new ArrayList<>());
        mayuGuildList.add(mayuGuild);
        saveGuildDatabase(true, true);
    }

    public void removeGuildFromDatabase(String guildID) {
        int counter = 0;
        for (MayuGuild mayuGuild : mayuGuildList) {
            if (mayuGuild.getGuild().getId().equals(guildID)) {
                mayuGuildList.remove(counter);
                saveGuildDatabase(true, true);
                break;
            }
            counter++;
        }
    }

    @SneakyThrows
    public void saveGuildDatabase(boolean log, boolean waitForThread) {
        Thread thread = new Thread(() -> {
            Thread.currentThread().setName("SaveGuildListThread");
            long start = System.currentTimeMillis();
            if (log) Logger.info("Saving guild database...");
            JsonUtilObject jsonUtilObject = JsonUtils.createOrLoadFile("guild_database.json");
            JsonObject finalJsonObject = new JsonObject();
            JsonArray guilds = new JsonArray();

            for (MayuGuild mayuGuild : mayuGuildList) {
                guilds.add(mayuGuild.getJsonObject());
            }

            finalJsonObject.add("guilds", guilds);
            jsonUtilObject.setJsonObject(finalJsonObject);
            jsonUtilObject.saveJson();

            if (log) Logger.success("Saved guild database, took " + (System.currentTimeMillis() - start) + "ms!");
        });
        thread.start();
        if (waitForThread)
            thread.join();
    }

    // Adders //
    public void addManagedMessage(String guildID, ManagedMessage managedMessage) {
        MayuGuild mayuGuild = getGuild(guildID);
        mayuGuild.addManagedMessage(managedMessage);
        updateGuildList(mayuGuild);
        saveGuildDatabase(false, false);
    }

    // Getters inside //
    private JsonObject getGuildDatabaseJson() {
        try {
            return JsonUtils.createOrLoadFile("guild_database.json").getJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("### FATAL ERROR ### Failed to load guild_database.json! Please fix it.");
            System.exit(-1);
        }
        return null;
    }
}
