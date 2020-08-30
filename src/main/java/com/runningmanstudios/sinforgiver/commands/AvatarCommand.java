package com.runningmanstudios.sinforgiver.commands;

import com.runningmanstudios.sinforgiver.commandUtil.Command;
import com.runningmanstudios.sinforgiver.commandUtil.CommandBuilder;
import com.runningmanstudios.sinforgiver.events.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.List;
import java.util.Random;

@CommandBuilder(name = "avatar", description = "find your avatar", usage = "@user?", aliases = {"icon", "pfp"})
public class AvatarCommand implements Command {
    Random r = new Random();

    @Override
    public void run(CommandEvent message) {
        List<Member> members = message.getMessage().getMentionedMembers();
        if (members.isEmpty()) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(new Color(0, 155, 255))
                    .setTitle("Your avatar")
                    .setImage(message.getAuthor().getAvatarUrl());
            message.getChannel().sendMessage(embed.build()).queue();
        } else {
            Member member = members.get(0);
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(new Color(0, 155, 255))
                    .setTitle(member.getUser().getName()+"'s avatar")
                    .setImage(member.getUser().getAvatarUrl());
            message.getChannel().sendMessage(embed.build()).queue();
        }
    }
}
