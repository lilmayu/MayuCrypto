package me.lilmayu.mayuCrypto.main.configUtils;

import com.google.gson.JsonObject;
import me.lilmayu.mayuCrypto.main.utils.json.JsonUtils;
import me.lilmayu.mayuCrypto.main.utils.json.objects.JsonUtilObject;

public class BotConfig {

    private String discordToken, prefix;

    public BotConfig(String filename) {
        JsonUtilObject jsonUtilObject = JsonUtils.createOrLoadFile(filename);
        JsonObject jsonObject = jsonUtilObject.getJsonObject();
        if (jsonObject.has("discordToken")) {
            discordToken = jsonObject.get("discordToken").getAsString();
        } else {
            jsonObject.addProperty("discordToken", "### YOUR DISCORD TOKEN HERE ###");
        }
        if (jsonObject.has("prefix")) {
            prefix = jsonObject.get("prefix").getAsString();
        } else {
            jsonObject.addProperty("prefix", "### PREFIX HERE ###");
        }
        jsonUtilObject.saveJson();
    }

    public boolean isValid() {
        return discordToken != null && prefix != null;
    }

    public String getDiscordToken() {
        return discordToken;
    }

    public String getPrefix() {
        return prefix;
    }

}
