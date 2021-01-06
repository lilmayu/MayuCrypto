package me.lilmayu.mayuCrypto.main.utils;

import me.lilmayu.mayuCrypto.main.Main;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.Instant;

public class ExceptionInformer implements Thread.UncaughtExceptionHandler {

    public static void registerExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionInformer());
        System.setProperty("sun.awt.exception.handler", ExceptionInformer.class.getName());
    }

    public static void handle(Throwable throwable) {
        try {
            throwable.printStackTrace();

            Logger.debug("Sending exception to discord...");
            String textChannelID = Main.getBotConfig().getExceptionMessageChannelID();
            if (textChannelID == null) {
                Logger.warning("ExceptionMessageChannelID is null, can't send exception!");
                return;
            }
            TextChannel textChannel = Main.getJDAApi().getTextChannelById(textChannelID);
            if (textChannel == null) {
                Logger.warning("Although textChannelID isn't null, textChannel null is.");
                return;
            }
            String name = throwable.getClass().getSimpleName();
            StackTraceElement[] stackTraceElements = throwable.getStackTrace();
            StringBuilder stackTrace = new StringBuilder("```\n");
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                if (stackTrace.toString().length() + stackTraceElement.toString().length() >= 1024) {
                    if (stackTrace.toString().length() <= 1020) {
                        stackTrace.append("...");
                    }
                    break;
                }
                stackTrace.append(stackTraceElement.toString()).append("\n");
            }
            stackTrace.append("```");
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("Exception occurred!");

            embedBuilder.setDescription(name);
            embedBuilder.addField("***Full stack trace***", stackTrace.toString(), false);

            embedBuilder.setFooter("Powered by MayuCrypto");
            embedBuilder.setTimestamp(Instant.now());
            textChannel.sendMessage(embedBuilder.build()).queue();
        } catch (Throwable e) {
            e.printStackTrace();
            Logger.error("Could not send exception message to Exception Message Channel!");
        }
    }

    public void uncaughtException(Thread t, Throwable e) {
        handle(e);
    }
}
