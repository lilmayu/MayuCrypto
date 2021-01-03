package me.lilmayu.mayuCrypto.main.utils.logger;

import me.lilmayu.mayuCrypto.main.utils.Utils;
import me.lilmayu.mayuCrypto.main.utils.colors.Color;
import me.lilmayu.mayuCrypto.main.utils.colors.Colors;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    public static void info(String s) {
        makeLog(LogType.INFO, Utils.currentMethod(2), s);
    }

    public static void warning(String s) {
        makeLog(LogType.WARNING, Utils.currentMethod(2), s);
    }

    public static void error(String s) {
        makeLog(LogType.ERROR, Utils.currentMethod(2), s);
    }

    public static void debug(String s) {
        makeLog(LogType.DEBUG, Utils.currentMethod(2), s);
    }

    public static void command(String s) {
        makeLog(LogType.COMMAND, Utils.currentMethod(2), s);
    }

    public static void success(String s) {
        makeLog(LogType.SUCCESS, Utils.currentMethod(2), s);
    }

    // // // // //

    private static void makeLog(LogType type, String methodName, String s) {
        String finalLog = "";
        switch (type) {
            case INFO:
                finalLog += new Color().setForeground(Colors.LIGHT_BLUE).build();
                break;
            case WARNING:
                finalLog += new Color().setForeground(Colors.LIGHT_YELLOW).build();
                break;
            case ERROR:
                finalLog += new Color().setForeground(Colors.RED).build();
                break;
            case DEBUG:
            case COMMAND:
                finalLog += new Color().setForeground(Colors.DARK_GRAY).build();
                break;
            case SUCCESS:
                finalLog += new Color().setForeground(Colors.LIGHT_GREEN).build();
                break;
            default:
                finalLog += new Color().setForeground(Colors.MAGENTA).build();
        }

        finalLog += "[" + getTime() + "] [MayuCrypto/" + Thread.currentThread().getName() + "] ";
        finalLog += "[" + methodName + "] ";
        finalLog += "[" + type.toString().toUpperCase() + "]: ";
        finalLog += s;
        finalLog += Color.RESET;
        System.out.println(finalLog);
    }

    private static String getTime() {
        return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
    }
}
