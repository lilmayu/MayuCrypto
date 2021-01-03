package me.lilmayu.mayuCrypto.main.utils.logger;

public enum LogType {

    INFO,
    WARNING,
    ERROR,
    DEBUG,
    COMMAND,
    SUCCESS;

    @Override
    public String toString() {
        switch (this) {
            case INFO:
                return "info";
            case WARNING:
                return "warning";
            case ERROR:
                return "error";
            case DEBUG:
                return "debug";
            case COMMAND:
                return "command";
            case SUCCESS:
                return "success";
        }
        return "null";
    }
}
