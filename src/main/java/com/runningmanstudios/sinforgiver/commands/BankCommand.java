package com.runningmanstudios.sinforgiver.commands;

import com.runningmanstudios.sinforgiver.commandUtil.Command;
import com.runningmanstudios.sinforgiver.commandUtil.CommandBuilder;
import com.runningmanstudios.sinforgiver.events.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@CommandBuilder(name = "bank", description = "bank command", usage = "@user?")
public class BankCommand implements Command {
    @Override
    public void run(CommandEvent message) {
        try {
            List<Member> members = message.getMessage().getMentionedMembers();
            if (members.isEmpty()) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(new Color(255, 0, 0))
                        .setAuthor(message.getAuthor().getName())
                        .setThumbnail(message.getAuthor().getAvatarUrl())
                        .addField("\uD83D\uDCB8", "||"+message.getEventDetector().getBot().users.getSection(message.getAuthor().getId()).get("coins").toString()+"||", true);
                // failure is always a Throwable
                message.getChannel().sendMessage(embed.build()).queue((result) -> result.delete().queueAfter(10, TimeUnit.SECONDS), Throwable::printStackTrace);
            } else {
                Member member = members.get(0);
                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(new Color(255, 0, 0))
                        .setAuthor(member.getUser().getName())
                        .setThumbnail(member.getUser().getAvatarUrl())
                        .addField("\uD83D\uDCB8", "||"+message.getEventDetector().getBot().users.getSection(member.getUser().getId()).get("coins").toString()+"||", true);
                message.getChannel().sendMessage(embed.build()).queue((result) -> result.delete().queueAfter(10, TimeUnit.SECONDS), Throwable::printStackTrace);
            }
        } catch (NullPointerException e) {
            message.getChannel().sendMessage("I couldn't find the person you're looking for").queue((result) -> result.delete().queueAfter(5, TimeUnit.SECONDS), Throwable::printStackTrace);
        }
    }
}
