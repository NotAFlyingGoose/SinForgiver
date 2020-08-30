package com.runningmanstudios.sinforgiver.commands;

import com.runningmanstudios.sinforgiver.commandUtil.Command;
import com.runningmanstudios.sinforgiver.commandUtil.CommandBuilder;
import com.runningmanstudios.sinforgiver.events.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@CommandBuilder(name = "level", description = "level command", usage = "@user?")
public class LevelCommand implements Command {
    @Override
    public void run(CommandEvent message) {
        try {
            List<Member> members = message.getMessage().getMentionedMembers();
            if (members.isEmpty()) {
                long xpToNextLvl = ((Number) message.getEventDetector().getBot().users.getSection(message.getAuthor().getId()).get("level")).longValue() * 150;
                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(new Color(0, 255, 0))
                        .setAuthor("Level")
                        .setTitle(message.getAuthor().getName()+"'s Levels")
                        .setThumbnail(message.getAuthor().getAvatarUrl())
                        .addField("Current Level \uD83D\uDD3C", message.getEventDetector().getBot().users.getSection(message.getAuthor().getId()).get("level").toString(), true)
                        .addField("Current Xp ✨", message.getEventDetector().getBot().users.getSection(message.getAuthor().getId()).get("xp").toString() + "/" + xpToNextLvl, true);
                // failure is always a Throwable
                message.getChannel().sendMessage(embed.build()).queue((result) -> result.delete().queueAfter(5, TimeUnit.SECONDS), Throwable::printStackTrace);
            } else {
                Member member = members.get(0);
                long xpToNextLvl = ((Number) message.getEventDetector().getBot().users.getSection(member.getUser().getId()).get("level")).longValue() * 150;
                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(new Color(0, 255, 0))
                        .setAuthor("Level")
                        .setTitle(member.getUser().getName()+"'s Levels")
                        .setThumbnail(member.getUser().getAvatarUrl())
                        .addField("Current Level \uD83D\uDD3C", message.getEventDetector().getBot().users.getSection(member.getUser().getId()).get("level").toString(), true)
                        .addField("Current Xp ✨", message.getEventDetector().getBot().users.getSection(member.getUser().getId()).get("xp").toString() + "/" + xpToNextLvl, true);
                // failure is always a Throwable
                message.getChannel().sendMessage(embed.build()).queue((result) -> result.delete().queueAfter(5, TimeUnit.SECONDS), Throwable::printStackTrace);
            }
        } catch (NullPointerException e) {
            message.getChannel().sendMessage("I couldn't find the person you're looking for").queue((result) -> result.delete().queueAfter(5, TimeUnit.SECONDS), Throwable::printStackTrace);
        }
    }
}
