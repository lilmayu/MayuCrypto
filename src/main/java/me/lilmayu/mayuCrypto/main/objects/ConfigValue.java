package me.lilmayu.mayuCrypto.main.objects;

import com.google.gson.JsonArray;
import lombok.Getter;
import lombok.NonNull;

public class ConfigValue {

    private @Getter String name;
    private @Getter Object value;

    public ConfigValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public Number getValueAsNumber() {
        return (Number) value;
    }

    public String getValueAsString() {
        return (String) value;
    }

    public Boolean getValueAsBoolean() {
        return (Boolean) value;
    }

    public JsonArray getValueAsJsonArray() {
        return (JsonArray) value;
    }
}
