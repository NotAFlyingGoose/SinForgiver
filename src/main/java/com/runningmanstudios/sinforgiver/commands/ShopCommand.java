package com.runningmanstudios.sinforgiver.commands;

import com.runningmanstudios.sinforgiver.commandUtil.Command;
import com.runningmanstudios.sinforgiver.data.DataBase;
import com.runningmanstudios.sinforgiver.commandUtil.CommandBuilder;
import com.runningmanstudios.sinforgiver.events.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;

import java.awt.*;

@CommandBuilder(name = "shop", description = "shop for buying items")
public class ShopCommand implements Command {
    @Override
    public void run(CommandEvent command) {
        try {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Shopping Center")
                    .setColor(new Color(255, 0, 0))
                    .setFooter("buy an item with `" + command.getEventDetector().getPrefix() + "buy <item id>`");
            DataBase items = command.getEventDetector().findJSON("items.json");
            JSONObject shop = command.getEventDetector().findJSON("data.json").getSection("shop");
            for (Object item : shop.keySet()) {
                embed.addField(
                        items.getSection(item.toString()).get("name").toString(),
                        "**- Icon:** " + items.getSection(item.toString()).get("icon").toString() + " \n" +
                                "**- Price:** " + (Integer.parseInt(shop.get(item).toString())*command.getEventDetector().getBot().getItemRarity(items.getSection(item.toString()).get("rarity").toString())) + " \n" +
                                "**- Rarity:** " + command.getEventDetector().getBot().ItemRarityString(items.getSection(item.toString()).get("rarity").toString()) + " \n" +
                                "**- Item id:** `" + item.toString() + "`", true);
            }

            command.getChannel().sendMessage(embed.build()).queue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
