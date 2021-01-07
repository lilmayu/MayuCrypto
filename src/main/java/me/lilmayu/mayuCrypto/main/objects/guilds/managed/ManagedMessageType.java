package me.lilmayu.mayuCrypto.main.objects.guilds.managed;

public enum ManagedMessageType {
    LIVE_CHART;

    public static ManagedMessageType fromString(String type) {
        switch (type) {
            case "LIVE_CHART": case "LIVE_STATS":
                return ManagedMessageType.LIVE_CHART;
        }
        return null;
    }
}
