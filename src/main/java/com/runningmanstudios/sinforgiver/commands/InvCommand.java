package com.runningmanstudios.sinforgiver.commands;

import com.runningmanstudios.sinforgiver.commandUtil.Command;
import com.runningmanstudios.sinforgiver.commandUtil.CommandBuilder;
import com.runningmanstudios.sinforgiver.data.DataBase;
import com.runningmanstudios.sinforgiver.events.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;

@CommandBuilder(name = "inv", description = "see your inventory")
public class InvCommand implements Command {
    @Override
    public void run(CommandEvent command) {
        try {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(command.getAuthor().getName())
                    .setColor(new Color(255, 0, 0));
            DataBase items = command.getEventDetector().findJSON("items.json");
            JSONObject shop = command.getEventDetector().findJSON("data.json").getSection("shop");
            JSONObject inv = (JSONObject) command.getEventDetector().getUserData(command.getAuthor()).get("inv");
            for (Object item : inv.keySet()) {
                int amt = Integer.parseInt(inv.get(item.toString()).toString());
                embed.addField(
                        items.getSection(item.toString()).get("name").toString(),
                        "**- Icon:** " + items.getSection(item.toString()).get("icon").toString() + " \n" +
                                "**- Sell Price:** " + (shop.get(item)!=null?((Number)shop.get(item)).intValue()*command.getEventDetector().getBot().getItemRarity(item.toString())/2:0) + " \n" +
                                "**- Rarity:** " + command.getEventDetector().getBot().ItemRarityString(items.getSection(item.toString()).get("rarity").toString()) + " \n" +
                                "**- Amount:** " + amt + " \n" +
                                "**- Item id:** `" + item.toString() + "`", true);
            }

            command.getChannel().sendMessage(embed.build()).queue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
