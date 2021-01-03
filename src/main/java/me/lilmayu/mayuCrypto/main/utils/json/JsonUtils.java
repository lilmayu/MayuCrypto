package me.lilmayu.mayuCrypto.main.utils.json;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
}