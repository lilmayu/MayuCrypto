package me.lilmayu.mayuCrypto.main.utils.json;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.lilmayu.mayuCrypto.main.exceptions.InvalidConfigValue;
import me.lilmayu.mayuCrypto.main.objects.ConfigValue;
import me.lilmayu.mayuCrypto.main.utils.json.objects.JsonUtilObject;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonUtils {

    /**
     * Creates json file at specific path
     *
     * @param path Path where will be json file created
     *
     * @return Returns JsonUtilObject
     */
    public static JsonUtilObject createOrLoadFile(String path) {
        return createOrLoadFile(Paths.get(path));
    }

    /**
     * Creates json file at specific path
     *
     * @param path Path where will be json file created
     *
     * @return Returns JsonUtilObject
     */
    public static JsonUtilObject createOrLoadFile(Path path) {
        File file = new File(FilenameUtils.getPath(path.toString()));
        if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS) && (path.toString().contains("/") || path.toString().contains("\\"))) {
            file.mkdirs();
        }

        file = path.toFile();

        if (!file.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(file.getPath());
                fileWriter.write("{}");
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (String fileLine : Files.readAllLines(path)) {
                stringBuilder.append(fileLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(stringBuilder.toString());
        return new JsonUtilObject(file, jsonObject);
    }

    public static void addToJsonObject(JsonObject jsonObject, String name, Object object) {
        if (object instanceof Number)
            jsonObject.addProperty(name, (Number) object);
        else if (object instanceof String)
            jsonObject.addProperty(name, (String) object);
        else if (object instanceof Boolean)
            jsonObject.addProperty(name, (Boolean) object);
        else if (object instanceof JsonArray)
            jsonObject.add(name, (JsonArray) object);
        else if (object == null) {
            jsonObject.add(name, null);
        } else {
            throw new RuntimeException(new InvalidConfigValue(new ConfigValue(name, object)));
        }
    }
}