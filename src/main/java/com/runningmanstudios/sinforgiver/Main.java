package com.runningmanstudios.sinforgiver;

import com.runningmanstudios.sinforgiver.commands.*;
import com.runningmanstudios.sinforgiver.commands.dungeon.DungeonCommand;

public class Main {
    public static void main(String[] args) {
        Bot bot = new Bot("/sinforgiver/data/");
        bot.addCommand(new HelpCommand());
        bot.addCommand(new BankCommand());
        bot.addCommand(new LevelCommand());
        bot.addCommand(new ForgiveCommand());
        bot.addCommand(new AvatarCommand());
        bot.addCommand(new PingCommand());
        bot.addCommand(new PrayCommand());
        bot.addCommand(new ShopCommand());
        bot.addCommand(new BuyCommand());
        bot.addCommand(new DungeonCommand());
        bot.addCommand(new InvCommand());
    }
}
