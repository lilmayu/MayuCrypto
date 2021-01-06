package me.lilmayu.mayuCrypto.main.objects.guilds;

import lombok.Getter;
import me.lilmayu.mayuCrypto.main.Main;
import me.lilmayu.mayuCrypto.main.objects.guilds.managed.ManagedMessageType;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ManagedMessage {

    private @Getter Message message;
    private @Getter TextChannel textChannel;

    private @Getter String messageID;
    private @Getter String textChannelID;
    private @Getter ManagedMessageType type;

    private @Getter boolean textChannelValid = false;
    private @Getter boolean messageValid = false;
    private @Getter boolean isValidAtConstruct = false;

    public ManagedMessage(String messageID, String textChannelID, ManagedMessageType type) {
        this.messageID = messageID;
        this.textChannelID = textChannelID;
        if (type == null) {
            Logger.error("ManagedMessageType is null. ManagedMessage: TextChannelID = '" + textChannelID + "', MessageID = '" + messageID + "'");
            return;
        }
        this.type = type;
        isValidAtConstruct = true;
    }

    public boolean resolveMessageFromID() {
        if (!isValidAtConstruct) {
            Logger.warning("IsValidAtConstruct is false! ManagedMessage: TextChannelID = '" + textChannelID + "', MessageID = '" + messageID + "'");
            return false;
        }
        textChannel = Main.getJDAApi().getTextChannelById(textChannelID);
        if (textChannel == null) {
            Logger.error("TextChannelID is invalid. ManagedMessage: TextChannelID = '" + textChannelID + "', MessageID = '" + messageID + "'");
            return false;
        }
        textChannelValid = true;
        message = textChannel.retrieveMessageById(messageID).complete();
        if (message == null) {
            Logger.warning("MessageID is invalid, however this isn't critical error. ManagedMessage: TextChannelID = '" + textChannelID + "', MessageID = '" + messageID + "'");
        }
        messageValid = true;
        return true;
    }
}
