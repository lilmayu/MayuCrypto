package me.lilmayu.mayuCrypto.main.configUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import me.lilmayu.mayuCrypto.main.objects.ConfigValue;
import me.lilmayu.mayuCrypto.main.utils.json.JsonUtils;
import me.lilmayu.mayuCrypto.main.utils.json.objects.JsonUtilObject;

public class BotConfig {

    private @Getter final String discordToken, prefix;
    private @Getter final String exceptionMessageChannelID;

    public BotConfig(String filename) {
        JsonUtilObject jsonUtilObject = JsonUtils.createOrLoadFile(filename);

        JsonElement jsonElement;

        jsonElement = checkForValue(jsonUtilObject, new ConfigValue("discordToken", "### YOUR DISCORD TOKEN HERE ###"));
        discordToken = (jsonElement == null) ? null : jsonElement.getAsString();

        jsonElement = checkForValue(jsonUtilObject, new ConfigValue("prefix", "### PREFIX HERE ###"));
        prefix = (jsonElement == null) ? null : jsonElement.getAsString();

        jsonElement = checkForValue(jsonUtilObject, new ConfigValue("exceptionMessageChannelID", null));
        exceptionMessageChannelID = (jsonElement == null) ? null : jsonElement.getAsString();
    }

    public boolean isValid() {
        return discordToken != null && prefix != null;
    }

    /**
     * Checks for name of value, if found, it will return that value
     *
     * @param jsonUtilObject JsonUtilObject object
     * @param configValue    ConfigValue object
     *
     * @return Will be null if there isn't value with desired name
     */
    public JsonElement checkForValue(JsonUtilObject jsonUtilObject, ConfigValue configValue) {
        JsonObject jsonObject = jsonUtilObject.getJsonObject();
        String name = configValue.getName();
        if (jsonObject.has(name)) {
            return jsonObject.get(name);
        } else {
            JsonUtils.addToJsonObject(jsonObject, name, configValue.getValue());
            jsonUtilObject.setJsonObject(jsonObject);
            jsonUtilObject.saveJson();
        }
        return null;
    }
}
