package com.runningmanstudios.sinforgiver.commands;

import com.runningmanstudios.sinforgiver.commandUtil.Command;
import com.runningmanstudios.sinforgiver.commandUtil.CommandBuilder;
import com.runningmanstudios.sinforgiver.events.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@CommandBuilder(name = "ping", description = "check latency")
public class PingCommand implements Command {
    Random r = new Random();

    @Override
    public void run(CommandEvent message) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(new Color(0, 255, 155))
                .setTitle("Pong")
                .addField("Ping", "...", true)
                .addField("Websocket", "...", true);
        message.getChannel().sendMessage(embed.build()).queue( m -> {
            long ping = message.getMessage().getTimeCreated().until(m.getTimeCreated(), ChronoUnit.MILLIS);
            EmbedBuilder newEmbed = new EmbedBuilder()
                    .setColor(new Color(0, 255, 155))
                    .setTitle("Pong")
                    .addField("Ping", ping + " ms", true)
                    .addField("Websocket", message.getJDA().getGatewayPing() + " ms", true);
            m.editMessage(newEmbed.build()).queue((result) -> result.delete().queueAfter(5, TimeUnit.SECONDS), Throwable::printStackTrace);
        });
    }
}
