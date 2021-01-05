package me.lilmayu.mayuCrypto.main.objects;

import com.jagrosh.jdautilities.command.Command;

import java.util.List;

public abstract class MayuCommand extends Command {

    public abstract String getDescription();
    public abstract String getSyntax();
    public abstract List<String> getExamples();
}
