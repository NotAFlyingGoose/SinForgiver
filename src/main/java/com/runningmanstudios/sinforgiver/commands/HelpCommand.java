package com.runningmanstudios.sinforgiver.commands;

import com.runningmanstudios.sinforgiver.commandUtil.Command;
import com.runningmanstudios.sinforgiver.commandUtil.CommandBuilder;
import com.runningmanstudios.sinforgiver.events.CommandEvent;
import com.runningmanstudios.sinforgiver.events.EventDetector;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@CommandBuilder(name = "help",
        description = "provides a list of commands, or if you specify you can see mre data about a single command",
        usage = "<page/command name> <number>")
public class HelpCommand implements Command {

    @Override
    public void run(CommandEvent message) {
        if (message.getArgs()[0].equalsIgnoreCase("page")) {
            int page = Integer.parseInt(message.getArgs()[1]);
            Set<Command> commands = message.getEventDetector().getCommands();
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Page " + page)
                    .setTitle("📄 - A List of all my commands - 📄")
                    .setColor(new Color(255, 255, 0))
                    .setTimestamp(null);
            for (Command command : commands) {
                CommandBuilder builder = command.getClass().getAnnotation(CommandBuilder.class);

                embed.addField(
                        "🎟️ - " + builder.name() + " - 🎟️",
                        "**- Usage:** `" + message.getEventDetector().getPrefix() + EventDetector.getUsages(builder) + "` \n" +
                                "**- Description:** " + builder.description(), true);
            }
            embed.setFooter("to see more info on a specific command do `" + message.getEventDetector().getPrefix() + "help <command name>`");
            message.getChannel().sendMessage(embed.build()).queue();
        } else {
            Set<Command> commands = message.getEventDetector().getCommands();
            for (Command command : commands) {
                CommandBuilder builder = command.getClass().getAnnotation(CommandBuilder.class);

                if (builder.name().equals(message.getArg(0)) || Arrays.asList(builder.aliases()).contains(message.getArg(0))) {
                    String commandData = "🎟️ - " + builder.name() + " - 🎟️\n";
                    commandData+="**Usage :** `" + message.getEventDetector().getPrefix() + EventDetector.getUsages(builder) + "` \n";
                    commandData+="**Description :** " + builder.description() + " \n";
                    commandData+="**Aliases :** `" + Arrays.toString(builder.aliases()) + "` \n";
                    message.getChannel().sendMessage(commandData).queue();
                    return;
                }
            }
            message.reply("That is not a valid command.").queue((result) -> result.delete().queueAfter(5, TimeUnit.SECONDS), Throwable::printStackTrace);
        }
    }
}
