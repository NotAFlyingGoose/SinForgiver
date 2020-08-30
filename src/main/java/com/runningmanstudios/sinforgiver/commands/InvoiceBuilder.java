package com.runningmanstudios.sinforgiver.commands;

import com.runningmanstudios.sinforgiver.events.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.awt.*;

public class InvoiceBuilder {
    public static MessageAction createInvoice(CommandEvent event, String item, int price) {
        return event.getChannel().sendMessage(new EmbedBuilder()
                .setColor(new Color(55, 55, 75))
                .setAuthor("InvoiceBuilder")
                .setTitle(event.getAuthor().getName() + " bought " + item)
                .setImage("https://i.imgur.com/Vhtwgn0.jpg")
                .addField("You spent", price + "", true)
                .build());
    }
}
