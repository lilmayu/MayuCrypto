package me.lilmayu.mayuCrypto.main.objects.guilds.managed;

public enum ManagedTextChannelType {
    UPDATE_PRICE_JUMPS;

    public static ManagedTextChannelType fromString(String type) {
        switch (type) {
            case "UPDATE_PRICE_JUMPS":
                return ManagedTextChannelType.UPDATE_PRICE_JUMPS;
        }
        return null;
    }
}
