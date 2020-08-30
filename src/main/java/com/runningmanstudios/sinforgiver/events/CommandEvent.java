package com.runningmanstudios.sinforgiver.events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class CommandEvent extends GuildMessageReceivedEvent {
    String[] args;
    EventDetector eventDetector;

    public CommandEvent(GuildMessageReceivedEvent message, String[] args, EventDetector eventDetector) {
        super(message.getJDA(), message.getResponseNumber(), message.getMessage());
        this.args = args;
        this.eventDetector = eventDetector;
    }

    public String[] getArgs() {
        return args;
    }

    public String getArg(int index) {
        return args[index];
    }

    @Nonnull
    @CheckReturnValue
    public MessageAction reply(@Nonnull CharSequence message)
    {
        return getChannel().sendMessage(createMention(getAuthor()) + ", " + message);
    }

    @Nonnull
    @CheckReturnValue
    public MessageAction reply(@Nonnull Message message)
    {
        return getChannel().sendMessage(createMention(getAuthor()) + ", " + message.getContentRaw());
    }

    public static String createMention(User user) {
        return "<@!"+user.getId()+">";
    }

    public EventDetector getEventDetector() {
        return eventDetector;
    }
}
