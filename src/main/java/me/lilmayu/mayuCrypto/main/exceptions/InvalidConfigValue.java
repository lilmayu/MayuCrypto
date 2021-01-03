package me.lilmayu.mayuCrypto.main.exceptions;

import me.lilmayu.mayuCrypto.main.objects.ConfigValue;

public class InvalidConfigValue extends Exception {

    public InvalidConfigValue(ConfigValue configValue) {
        super("ConfigValue with value '" + configValue.getValue().toString() + "' is invalid!");
    }
}
