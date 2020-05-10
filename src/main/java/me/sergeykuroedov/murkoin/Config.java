package me.sergeykuroedov.murkoin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private Properties properties;
    private String discordToken, prefix;

    private void loadConfig(String filename) throws IOException {
        File configFile = new File(filename);
        FileInputStream fileInputStream = new FileInputStream(configFile);
        properties = new Properties();
        properties.load(fileInputStream);
    }

    public Config(String filename) {
        try {
            loadConfig(filename);
            discordToken = properties.getProperty("discordToken");
            prefix = properties.getProperty("prefix");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDiscordToken() {
        return discordToken;
    }

    public String getPrefix() {
        return prefix;
    }

}
