package com.runningmanstudios.sinforgiver.commands;

import com.runningmanstudios.sinforgiver.commandUtil.Command;
import com.runningmanstudios.sinforgiver.commandUtil.CommandBuilder;
import com.runningmanstudios.sinforgiver.events.CommandEvent;

import java.util.Random;

@CommandBuilder(name = "pray", description = "pray to god by typing your prayer in the \"prayer\" field and get a verse from the bible about praying", usage = "text")
public class PrayCommand implements Command {
    Random r = new Random();

    @Override
    public void run(CommandEvent message) {
        int roll = r.nextInt(10);
        switch (roll) {
            case 0 -> message.getChannel().sendMessage("Rejoice always, pray without ceasing, give thanks in all circumstances; for this is the will of God in Christ Jesus for you (Thessalonians 5:16-18)").queue();
            case 1 -> message.getChannel().sendMessage("if my people, who are called by my name, will humble themselves and pray and seek my face and turn from their wicked ways, then I will hear from heaven, and I will forgive their sin and will heal their land. (Chronicles 7:14)").queue();
            case 2 -> message.getChannel().sendMessage("But I tell you, love your enemies and pray for those who persecute you, (Matthew 5:44)").queue();
            case 3 -> message.getChannel().sendMessage("Is anyone among you in trouble? Let them pray. Is anyone happy? Let them sing songs of praise. (James 5:13)").queue();
            case 4 -> message.getChannel().sendMessage("I call on you, my God, for you will answer me; turn your ear to me and hear my prayer. (Psalm 17:6)").queue();
            case 5 -> message.getChannel().sendMessage("The LORD is near to all who call on him, to all who call on him in truth. (Psalm 145:18)").queue();
            case 6 -> message.getChannel().sendMessage("One of those days Jesus went out to a mountainside to pray, and spent the night praying to God. (Luke 6:12)").queue();
            case 7 -> message.getChannel().sendMessage("Devote yourselves to prayer, being watchful and thankful. (Colossians 4:2)").queue();
            case 8 -> message.getChannel().sendMessage("pray continually, (Thessalonians 5:17)").queue();
            case 9 -> message.getChannel().sendMessage("God listened to your request, and said \"No.\" (God 666)").queue();
            default -> message.getChannel().sendMessage("That person should not expect to receive anything from the Lord. (James 1:7)").queue();
        }
    }
}
