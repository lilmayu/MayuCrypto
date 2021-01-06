package me.lilmayu.mayuCrypto.main.objects.guilds;

import lombok.Getter;
import me.lilmayu.mayuCrypto.main.Main;
import me.lilmayu.mayuCrypto.main.objects.guilds.managed.ManagedTextChannelType;
import me.lilmayu.mayuCrypto.main.utils.CryptoSymbol;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class ManagedTextChannel {

    private @Getter User user;
    private @Getter MessageChannel messageChannel;

    private @Getter String textChannelID;
    private @Getter boolean isUserA;
    private @Getter String userID;
    private @Getter ManagedTextChannelType type;
    private @Getter CryptoSymbol cryptoSymbol;
    private @Getter int jumpValue;

    private @Getter boolean userValid;
    private @Getter boolean messageChannelValid;
    private @Getter boolean isValidAtConstruct = false;

    public ManagedTextChannel(String textChannelID, boolean isUserA, String userID, ManagedTextChannelType type, CryptoSymbol cryptoSymbol, int jumpValue) {
        this.textChannelID = textChannelID;
        this.isUserA = isUserA;
        this.userID = userID;
        this.type = type;
        if (type == null) {
            Logger.error("ManagedTextChannelType is null. ManagedTextChannel: TextChannelID = '" + textChannelID + "'");
            return;
        }
        this.cryptoSymbol = cryptoSymbol;
        this.jumpValue = jumpValue;
        isValidAtConstruct = true;
    }

    public boolean resolveMessageChannelFromID() {
        if (!isValidAtConstruct) {
            Logger.warning("IsValidAtConstruct is false! ManagedTextChannel: TextChannelID = '" + textChannelID + "'");
            return false;
        }
        if (isUserA) {
            user = Main.getJDAApi().retrieveUserById(userID, false).complete();
            if (user != null) {
                userValid = true;
                messageChannel = user.openPrivateChannel().complete();
                messageChannelValid = true;
                return true;
            }
            return false;
        }
        messageChannel = Main.getJDAApi().getTextChannelById(textChannelID);
        if (messageChannel == null) {
            Logger.error("TextChannelID is invalid. ManagedTextChannel: TextChannelID = '" + textChannelID + "'");
            return false;
        }
        return true;
    }
}
