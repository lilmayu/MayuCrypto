package me.lilmayu.mayuCrypto.main.objects.guilds;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import me.lilmayu.mayuCrypto.main.configUtils.GuildConfig;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

public class MayuGuild {

    private @Getter Guild guild;
    private @Getter GuildConfig guildConfig; // TODO: Dát sem nějaké configy do guildy a pak udělat saving mechanismus
    private @Getter List<ManagedTextChannel> managedTextChannelList;
    private @Getter List<ManagedMessage> managedMessageList;

    public MayuGuild(@NonNull Guild guild, GuildConfig guildConfig, List<ManagedTextChannel> managedTextChannelList, List<ManagedMessage> managedMessageList) {
        this.guild = guild;
        this.guildConfig = guildConfig;
        this.managedTextChannelList = managedTextChannelList;
        this.managedMessageList = managedMessageList;
    }

    public JsonObject getJsonObject() {
        JsonObject guildJsonObject = new JsonObject();
        JsonArray managedMessagesJsonArray = new JsonArray();
        JsonArray managedTextChannelsJsonArray = new JsonArray();

        guildJsonObject.addProperty("guildID", guild.getId());

        for (ManagedMessage managedMessage : managedMessageList) {
            JsonObject managedMessageJson = new JsonObject();

            managedMessageJson.addProperty("messageID", managedMessage.getMessageID());
            managedMessageJson.addProperty("textChannelID", managedMessage.getTextChannelID());
            managedMessageJson.addProperty("type", managedMessage.getType().toString());

            JsonObject data = new JsonObject();

            data.addProperty("symbol", managedMessage.getDataCryptoSymbol().toString());

            managedMessageJson.add("data", data);

            managedMessagesJsonArray.add(managedMessageJson);
        }

        for (ManagedTextChannel managedTextChannel : managedTextChannelList) {
            JsonObject managedTextChannelJson = new JsonObject();
            JsonObject data = new JsonObject();

            managedTextChannelJson.addProperty("textChannelID", managedTextChannel.getTextChannelID());
            managedTextChannelJson.addProperty("isUser", managedTextChannel.isUserA());
            managedTextChannelJson.addProperty("userID", managedTextChannel.getUserID());
            managedTextChannelJson.addProperty("type", managedTextChannel.getType().toString());

            data.addProperty("symbol", managedTextChannel.getCryptoSymbol().toString());
            data.addProperty("jump-value", managedTextChannel.getJumpValue());

            managedTextChannelJson.add("data", data);

            managedTextChannelsJsonArray.add(managedTextChannelJson);
        }

        guildJsonObject.add("managedMessages", managedMessagesJsonArray);
        guildJsonObject.add("managedTextChannels", managedTextChannelsJsonArray);

        return guildJsonObject;
    }

    public void addManagedMessage(ManagedMessage managedMessage) {
        this.managedMessageList.add(managedMessage);
    }
}
