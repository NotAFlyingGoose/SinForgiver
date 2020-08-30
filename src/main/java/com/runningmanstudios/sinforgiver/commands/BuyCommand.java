package com.runningmanstudios.sinforgiver.commands;

import com.runningmanstudios.sinforgiver.commandUtil.Command;
import com.runningmanstudios.sinforgiver.data.DataBase;
import com.runningmanstudios.sinforgiver.commandUtil.CommandBuilder;
import com.runningmanstudios.sinforgiver.events.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;

import java.awt.*;

@CommandBuilder(name = "buy", description = "buy the items")
public class BuyCommand implements Command {
    @Override
    public void run(CommandEvent command) {
        DataBase items = command.getEventDetector().findJSON("items.json");
        JSONObject shop = command.getEventDetector().findJSON("data.json").getSection("shop");

        String item_id = command.getArg(0);

        command.reply(items.getSection(item_id).get("name").toString()).queue();
    }
}
