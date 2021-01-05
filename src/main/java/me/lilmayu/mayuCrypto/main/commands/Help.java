package me.lilmayu.mayuCrypto.main.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import lombok.Getter;
import me.lilmayu.mayuCrypto.main.Main;
import me.lilmayu.mayuCrypto.main.objects.MayuCommand;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Help extends Command {

    private static @Getter List<MayuCommand> commands = new ArrayList<>();

    public Help() {
        this.name = "Help";
        this.aliases = new String[]{"help"};
        this.guildOnly = false;
    }

    public static void addCommand(MayuCommand mayuCommand) {
        commands.add(mayuCommand);
    }

    @Override
    protected void execute(CommandEvent event) {
        String args = event.getArgs();
        String[] argsArr = args.split(" ");

        String commandHelp = null;
        if (argsArr.length == 1) {
            commandHelp = argsArr[0];
        }
        if (commandHelp != null) {
            boolean done = makeCommandSpecificHelp(event, commandHelp);
            if (done)
                return;
        }

        EmbedBuilder embedHelp = new EmbedBuilder();
        embedHelp.setTitle("MayuCrypto Commands");
        embedHelp.setDescription("Type `!mc help [command]` for command-specific help");
        embedHelp.setFooter("Powered by MayuCrypto");
        embedHelp.setTimestamp(Instant.now());

        String commandsString = "";
        int counter = 0;
        for (MayuCommand mayuCommand : commands) {
            commandsString += "`" + mayuCommand.getName() + "`";
            if (counter + 1 != commands.size()) {
                commandsString += ", ";
            }
            counter++;
        }
        if (commandsString.equals(""))
            commandsString = "None";

        embedHelp.addField("User commands", commandsString, true);
        event.reply(embedHelp.build());
    }

    public boolean makeCommandSpecificHelp(CommandEvent event, String commandName) {
        MayuCommand command = null;
        for (MayuCommand mayuCommand : commands) {
            if (mayuCommand.getName().toLowerCase().equals(commandName.toLowerCase())) {
                command = mayuCommand;
            }
        }
        if (command == null) {
            //event.reply("This command does not exist!");
            return false;
        }

        String commandFirst = Main.getBotConfig().getPrefix() + " " + command.getName().toLowerCase() + " ";

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Command - " + command.getName());
        embedBuilder.setDescription(command.getDescription());
        embedBuilder.setFooter("Powered by MayuCrypto");
        embedBuilder.setTimestamp(Instant.now());

        embedBuilder.addField("Sytanx", "`" + commandFirst + command.getSyntax() + "`", true);

        StringBuilder examples = new StringBuilder();
        for (String str : command.getExamples()) {
            examples.append("`").append(commandFirst).append(str).append("`\n");
        }

        StringBuilder aliases = new StringBuilder();
        for (String str : command.getAliases()) {
            aliases.append("`").append(str).append("`\n");
        }

        embedBuilder.addField("Examples", examples.toString(), false);
        embedBuilder.addField("Aliases", aliases.toString(), false);
        event.reply(embedBuilder.build());
        return true;
    }
}
