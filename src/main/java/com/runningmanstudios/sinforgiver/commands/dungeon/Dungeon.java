package com.runningmanstudios.sinforgiver.commands.dungeon;

import com.runningmanstudios.sinforgiver.commandUtil.Command;
import com.runningmanstudios.sinforgiver.data.DataBase;
import com.runningmanstudios.sinforgiver.events.CommandEvent;
import net.dv8tion.jda.api.entities.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Dungeon {
    public boolean running;
    User player;
    List<String> nextPatterns = new LinkedList<>();
    String lastShow = "";
    public Dungeon(User player, CommandEvent event) {
        nextPatterns.add("continue");
        running = true;
        this.player = player;
        JSONObject userData = event.getEventDetector().getUserData(event.getAuthor());
        if (!userData.containsKey("dungeon")) {
            JSONObject game = new JSONObject();
            game.put("rank", 1);
            game.put("magic", 2);
            game.put("monster", new JSONArray());
            game.put("mode", -2);
            userData.put("dungeon", game);

            event.getEventDetector().getBot().users.writeContent();
        } else {
            JSONObject previousGame = (JSONObject) userData.get("dungeon");
            if (((Number) previousGame.get("mode")).intValue()>=100 || ((Number) previousGame.get("mode")).intValue()<1) {
                previousGame.put("rank", 1);
                previousGame.put("magic", 2);
                previousGame.put("monster", new JSONArray());
                previousGame.put("mode", -2);
                userData.put("dungeon", previousGame);

                event.getEventDetector().getBot().users.writeContent();
            } else {
                previousGame.put("mode", 1);
                userData.put("dungeon", previousGame);

                event.getEventDetector().getBot().users.writeContent();
                lastShow = """
                ```md
                # type `continue` to continue your game...
                ```
                """;
                event.getChannel().sendMessage(lastShow).queue();

                event.getEventDetector().getBot().users.writeContent();
                return;
            }
        }

        lastShow = """
                ```md
                < you are walking around in mideval york city when a man stops you and says >
                # "Pardon me, adventurer. I'm relieved to see you. I need some coochie right now, my stocks have run low and I need it now! if I don't get coochie then i'll focking die!!! go, here is bag of coins so you know im not kidding around, please go get me some coochie!"
                < you go to find some coochie for the mysterious man. but when you're entering hooters you fall into a ditch, you drop your bag of coins and land in the sewer. eventualy you find your way out but now your in the middle of nowhere >
                /* As you walk through the forrest, you find yourself a comfortable log, take a bottle of lotion and the bible from your pouch, but then you hear a creak and swing around to see a giant stone gate. inside it is very dark and there are bat dropping everywhere. eventualy, through all the corradors you find a giant wooden door. you are about to leave when the door opens *
                
                
                # type `continue` to continue story...
                ```
                """;
        event.getChannel().sendMessage(lastShow).queue();
    }

    public void onResponse(CommandEvent event) {
        nextPatterns.clear();
        JSONObject userData = event.getEventDetector().getUserData(event.getAuthor());
        JSONObject dungeonData = (JSONObject) userData.get("dungeon");
        JSONObject monsters = event.getEventDetector().findJSON("data.json").getSection("monsters");
        int mode = ((Number) dungeonData.get("mode")).intValue();
        float rank = ((Number) dungeonData.get("rank")).floatValue();
        float magic = ((Number) dungeonData.get("magic")).floatValue();
        switch (mode) {
            case -2 -> {
                String menu = createFightMenu(new Fighter("Skella-Bone - 🦴", 1, 0), Fighter.createFromUser(player, event.getEventDetector()), getSays("Skella-Bone", "*rattle*"), new String[]{"next"});
                event.getChannel().sendMessage(menu).queue();
                dungeonData.put("mode", mode + 1);
                nextPatterns.add("1");
                lastShow = menu;
            }
            case -1 -> {
                String menu = createFightMenu(new Fighter("Skella-Bone - 🦴", 1, 0), Fighter.createFromUser(player, event.getEventDetector()), "The monster has the same level as you, so you can not fight him normally. you have to use your magic\nyou can use magic instead of rank to kill a monster, but afterwards you lose some magic", new String[]{"fight"});
                event.getChannel().sendMessage(menu).queue();
                dungeonData.put("mode", mode + 1);
                nextPatterns.add("1");
                lastShow = menu;
            }
            case 0 -> {
                dungeonData.put("rank", rank+1);
                dungeonData.put("magic", magic-1);
                String menu = createFightMenu(new Fighter("Skella-Bone - 🦴", 1, 0), Fighter.createFromUser(player, event.getEventDetector()), "you kill the Skella-Bone\nyou killed the monster but lost some magic, because you beat this monster, you up your rank,\nsometimes, you can't beat a monster, with or without magic, so just go away for a bit and you will gain magic from just chatting\n", new String[]{"continue through dungeon"});
                event.getChannel().sendMessage(menu).queue();
                dungeonData.put("mode", mode + 1);
                nextPatterns.add("1");
                lastShow = menu;
            }
            case 1 -> {
                String monsterName = "";
                float monsterRank = 0;
                if (((JSONArray)dungeonData.get("monster")).size()<2) {
                    int monstersLength = monsters.size();
                    double monsterIndex = (Math.floor(Math.random() * monstersLength));
                    int i = 0;
                    for (Object monster : monsters.keySet()) {
                        if (i == monsterIndex) {
                            monsterName = monster.toString();
                            break;
                        }
                        i++;
                    }
                } else {
                    monsterName = ((JSONArray)dungeonData.get("monster")).get(0).toString();
                    monsterRank = ((Number)((JSONArray)dungeonData.get("monster")).get(1)).floatValue();
                }
                Fighter player = Fighter.createFromUser(this.player, event.getEventDetector());

                JSONObject monsterData = (JSONObject)monsters.get(monsterName);
                String monsterLogo = monsterData.get("logo").toString();
                String monsterSays = monsterData.get("says").toString()
                        .replace("${said}", event.getMessage().getContentRaw())
                        .replace("${name}", event.getAuthor().getName());
                float monsterBase = ((Number)monsterData.get("base")).floatValue();
                if (((JSONArray)dungeonData.get("monster")).size()<2) {
                    int min = (int) (player.getRank()-2);
                    int max = (int) (player.getRank()+7);
                    monsterRank = Math.max(1, Math.max(min, monsterBase + (float) (new Random().nextInt(max - min) + min)));
                }
                Fighter monster = new Fighter(monsterName+" - "+monsterLogo, monsterRank, 0);

                String menu = createFightMenu(monster, player, getSays(monsterName, monsterSays), new String[]{"fight", "use magic"});
                lastShow = menu;

                event.getChannel().sendMessage(menu).queue();
                JSONArray monsterSave = new JSONArray();
                monsterSave.add(monsterName);
                monsterSave.add(monsterRank);

                dungeonData.put("monster", monsterSave);
                dungeonData.put("mode", mode+1);

                nextPatterns.add("1");
                nextPatterns.add("2");
            }
            case 2 -> {
                JSONArray fighting = (JSONArray) dungeonData.get("monster");
                String monsterName = fighting.get(0).toString();
                float monsterRank = ((Number)fighting.get(1)).floatValue();
                JSONObject monsterData = (JSONObject)monsters.get(monsterName);
                String monsterLogo = monsterData.get("logo").toString();

                Fighter monster = new Fighter(monsterName+" - "+monsterLogo, monsterRank, 0);
                Fighter player = Fighter.createFromUser(this.player, event.getEventDetector());

                int option = Integer.parseInt(event.getMessage().getContentRaw());
                if (option==1) {
                    if (monsterRank<player.getRank()) {
                        String menu = createFightMenu(monster, player, "you killed the " + monsterName, new String[]{"claim treasure"});
                        lastShow = menu;
                        event.getChannel().sendMessage(menu).queue();
                        dungeonData.put("rank", rank+1);
                        dungeonData.put("magic", magic+0.2);
                        dungeonData.put("mode", mode+1);

                        nextPatterns.add("1");
                    }
                    else {
                        String menu = createFightMenu(monster, player, "The monsters is generally just to good for your filthy \"rank\"", new String[]{"fight", "use magic"});
                        if (monsterData.get("onRank")!=null) menu = createFightMenu(monster, player, "The monsters is generally just to good for your filthy \"rank\", it says \""+monsterData.get("onRank").toString().replace("${said}", event.getMessage().getContentRaw()).replace("${name}", event.getAuthor().getName())+"\"", new String[]{"fight", "use magic"});
                        lastShow = menu;
                        event.getChannel().sendMessage(menu).queue();

                        nextPatterns.add("1");
                        nextPatterns.add("2");
                    }
                } else if (option==2) {
                    if (monsterRank<player.getMagic()) {
                        String menu = createFightMenu(monster, player, "you killed the " + monsterName, new String[]{"claim treasure"});
                        lastShow = menu;
                        event.getChannel().sendMessage(menu).queue();
                        dungeonData.put("rank", rank+1);
                        dungeonData.put("magic", magic-(monster.getRank()/2));
                        dungeonData.put("mode", mode+1);

                        nextPatterns.add("1");
                    }
                    else {
                        String menu = createFightMenu(monster, player, "The monster is stronger than your weak magic", new String[]{"fight", "use magic"});
                        if (monsterData.get("onMagic")!=null) menu = createFightMenu(monster, player, "The monster is stronger than your magic, it says \""+monsterData.get("onMagic").toString().replace("${said}", event.getMessage().getContentRaw()).replace("${name}", event.getAuthor().getName())+"\"", new String[]{"fight", "use magic"});
                        lastShow = menu;
                        event.getChannel().sendMessage(menu).queue();

                        nextPatterns.add("1");
                        nextPatterns.add("2");
                    }
                }
            }
            case 3 -> {
                JSONArray fighting = (JSONArray) dungeonData.get("monster");
                String monsterName = fighting.get(0).toString();
                float monsterRank = ((Number)fighting.get(1)).floatValue();
                JSONObject monsterData = (JSONObject)monsters.get(monsterName);
                String monsterLogo = monsterData.get("logo").toString();
                String monsterSays = monsterData.get("says").toString()
                        .replace("${said}", event.getMessage().getContentRaw())
                        .replace("${name}", event.getAuthor().getName());

                Fighter monster = new Fighter(monsterName+" - "+monsterLogo, monsterRank, 0);
                Fighter player = Fighter.createFromUser(this.player, event.getEventDetector());
                Random roll = new Random();

                int wtd = roll.nextInt(3);
                String menu;
                if (wtd==0) {
                    int coins = roll.nextInt((int) monsterRank*11);
                    menu = createTreasureMenu(player, coins, new String[]{}, event.getEventDetector().getBot().items, "you won "+coins+" coins", new String[]{"Go To Next Room"});
                } else if (wtd==1) {
                    int itemAmt = roll.nextInt((int) Math.max(1, Math.floor(monsterRank/2)));
                    List<String> items = new LinkedList<>();
                    JSONArray treasures = (JSONArray) event.getEventDetector().getBot().data.getSection("treasure").get("dungeon");
                    for (int i = 0; i < itemAmt; i++) {
                        items.add(event.getEventDetector().getBot().getItemsByRarity(treasures, 2));
                    }
                    String[] TreasureItems = new String[items.size()];
                    for (int i = 0; i < items.size(); i++) {
                        event.getEventDetector().giveUserItem(event.getAuthor(), items.get(i));
                        TreasureItems[i] = items.get(i);
                    }
                    menu = createTreasureMenu(player, 0, TreasureItems, event.getEventDetector().getBot().items, "you won "+TreasureItems.length+" items", new String[]{"Go To Next Room"});
                } else {
                    int coins = roll.nextInt((int) monsterRank*5);
                    int itemAmt = roll.nextInt((int) Math.max(1, Math.floor(monsterRank/4)));
                    List<String> items = new LinkedList<>();
                    JSONArray treasures = (JSONArray) event.getEventDetector().getBot().data.getSection("treasure").get("dungeon");
                    for (int i = 0; i < itemAmt; i++) {
                        items.add(event.getEventDetector().getBot().getItemsByRarity(treasures, 5));
                    }
                    String[] TreasureItems = new String[items.size()];
                    for (int i = 0; i < items.size(); i++) {
                        event.getEventDetector().giveUserItem(event.getAuthor(), items.get(i));
                        TreasureItems[i] = items.get(i);
                    }
                    menu = createTreasureMenu(player, coins, TreasureItems, event.getEventDetector().getBot().items, "you won "+coins+" and "+TreasureItems.length+" items", new String[]{"Go To Next Room"});
                }
                lastShow = menu;
                event.getChannel().sendMessage(menu).queue();
                dungeonData.put("monster", new JSONArray());
                dungeonData.put("mode", 1);

                nextPatterns.add("1");
            }
        }
        event.getEventDetector().getBot().users.writeContent();
    }

    public String getSays(String name, String says) {
        return "you come across a " + name + " it looks at you, \"" + says + "\" it says,\nuh oh, i think we have to FIGHT";
    }

    public String createFightMenu(Fighter monster, Fighter player, String message, String[] options) {
        StringBuilder str = new StringBuilder("```md\n" +
                "> -----FIGHT-----\n" +
                "< MONSTER NAME: >/* " + monster.getName() + " *\n" +
                "< MONSTER RANK: >/* " + monster.getRank() + " *\n\n" +
                "# VS\n\n" +
                "< YOUR NAME: >/* " + player.getName() + " *\n" +
                "< YOUR RANK: >/* " + player.getRank() + " *\n" +
                "< YOUR MAGIC: >/* " + player.getMagic() + " *\n\n" +
                "OPTIONS\n" +
                "=======\n");
        for (int i = 0; i < options.length; i++) {
            str.append("[").append(i+1).append("]: ").append(options[i].toUpperCase()).append('\n');
        }
        str.append("[").append(options.length+1).append("]: ").append("EXIT").append('\n');
        str.append("\n").append(message).append("\n\n\n").append("```");
        return str.toString();
    }

    private String createTreasureMenu(Fighter player, int coins, String[] items, DataBase itemData, String message, String[] options) {
        StringBuilder str = new StringBuilder("```md\n" +
                "> -----TREASURE-----\n" +
                "< COINS WON: >/* " + coins + " *\n" +
                "< ITEMS WON: >/* -");
        for (String item : items) {
            str.append(itemData.getSection(item).get("name"))
                    .append(" ")
                    .append(itemData.getSection(item).get("icon"))
                    .append("-");
        }
        str.append(" *\n\n" +
                "< YOUR NAME: >/* " + player.getName() + " *\n" +
                "< YOUR RANK: >/* " + player.getRank() + " *\n" +
                "< YOUR MAGIC: >/* " + player.getMagic() + " *\n\n" +
                "OPTIONS\n" +
                "=======\n");
        for (int i = 0; i < options.length; i++) {
            str.append("[").append(i+1).append("]: ").append(options[i].toUpperCase()).append('\n');
        }
        str.append("[").append(options.length+1).append("]: ").append("EXIT").append('\n');
        str.append("\n").append(message).append("\n\n\n").append("```");
        return str.toString();
    }

    public List<String> getNextPatterns() {
        return nextPatterns;
    }

    public String getLastShow() {
        return lastShow;
    }
}
