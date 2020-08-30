package com.runningmanstudios.sinforgiver.commands;

import com.runningmanstudios.sinforgiver.commandUtil.Command;
import com.runningmanstudios.sinforgiver.commandUtil.CommandBuilder;
import com.runningmanstudios.sinforgiver.events.CommandEvent;

import java.util.Random;

@CommandBuilder(name = "forgive", description = "god forgives you of your sins", usage = "<text>", aliases = {"forgiveme"})
public class ForgiveCommand implements Command {
    Random r = new Random();
    @Override
    public void run(CommandEvent message) {
        int roll = r.nextInt(3);
        switch (roll) {
            case 0 -> message.getChannel().sendMessage("May the Almighty and merciful Lord grant us pardon\nabsolution and remission of our sins. By his authority, I absolve " + message.getAuthor().getName() + " from his sins,\nin the name of the Father, and of the Holy Spirit. Amen.\nhttps://i.imgur.com/RirGUbl.jpg").queue();
            case 1 -> message.getChannel().sendMessage("Pray and you may find the answers you seeketh\nI absolve " + message.getAuthor().getName() + " from his sins,\nin the name of the Father, and of the Holy Spirit. Amen.\nhttps://i.imgur.com/Xm27PCH.jpg").queue();
            case 2 -> message.getChannel().sendMessage("You think to sin like that is funny?\nThe holy Father is not pleased with your acts of treason!\nYou must get what you deserve!\nhttps://i.imgur.com/EltegyI.jpg").queue();
        }
    }
}
