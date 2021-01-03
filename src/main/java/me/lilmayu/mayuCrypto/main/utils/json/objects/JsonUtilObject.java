package me.lilmayu.mayuCrypto.main.utils.json.objects;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import me.lilmayu.mayuCrypto.main.utils.json.JsonUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonUtilObject {

    private @Getter @Setter File file;
    private @Getter @Setter JsonObject jsonObject;

    public JsonUtilObject(File file, JsonObject jsonObject) {
        this.file = file;
        this.jsonObject = jsonObject;
    }

    /**
     * Loads current json file in path
     */
    public void reloadJson() {
        this.jsonObject = JsonUtils.createOrLoadFile(file.getPath()).getJsonObject();
    }

    /**
     * Pretty saves current JsonObject
     */
    public void saveJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jsonObject.toString());
        String prettyJsonString = gson.toJson(je);

        try {
            FileWriter fileWriter = new FileWriter(file.getPath());
            fileWriter.write(prettyJsonString);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
