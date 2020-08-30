package com.runningmanstudios.sinforgiver.events;

import com.runningmanstudios.sinforgiver.Bot;
import com.runningmanstudios.sinforgiver.commandUtil.AttractInfo;
import com.runningmanstudios.sinforgiver.commandUtil.AttractableCommand;
import com.runningmanstudios.sinforgiver.commandUtil.Command;
import com.runningmanstudios.sinforgiver.commandUtil.CommandBuilder;
import com.runningmanstudios.sinforgiver.data.DataBase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class EventDetector extends ListenerAdapter {
    DecimalFormat df = new DecimalFormat("0.##");

    Map<String, AttractInfo> attractors = new HashMap<>();

    String prefix = "sin ";
    Set<Command> commandSet = new HashSet<>();
    String[] newChannel = new String[] {"wow this looks roomy!", "can't wait to see what this becomes", "*whistling*"};
    Random random = new Random();
    Bot bot;

    public EventDetector(Bot bot) {
        this.bot = bot;
    }

    public void addCommand(Command command) {
        commandSet.add(command);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("The Bot is Ready for action!");
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getMember().getUser().isBot()) return;

        if (!bot.users.containsSection(event.getAuthor().getId())) {
            bot.users.addSection(event.getAuthor().getId());
        }

        AttractInfo attractor = attractors.get(event.getAuthor().getId());
        if (attractor!=null) {
            Instant then = attractor.getStart();
            Instant now = Instant.now();
            Duration timeElapsed = Duration.between(then, now);
            if (attractor.textEquals(event.getMessage().getContentRaw()) && !(timeElapsed.toMinutes()>30)) {
                attractor.getCommand().onAttract(new CommandEvent(event, event.getMessage().getContentRaw().split(" "), this));
                attractor.updateTime();
                return;
            } else stopAttracting(event.getAuthor());
        }

        JSONObject user = bot.users.getSection(event.getAuthor().getId());
        if (!user.containsKey("coins")) {
            user.put("coins", 0);
        }
        if (!user.containsKey("xp")) {
            user.put("xp", 0);
        }
        if (!user.containsKey("level")) {
            user.put("level", 1);
        }
        Random roll = new Random();

        //coins
        int coinAmt = roll.nextInt(15) + 1;
        int coinBase = roll.nextInt(15) + 1;

        if (coinAmt == coinBase) {
            long coins = (long) user.get("coins");
            user.replace("coins", coins + coinAmt);
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(new Color(255, 0, 0))
                    .setAuthor(event.getAuthor().getName())
                    .addField("\uD83D\uDCB8", coinAmt + " coins added!", true)
                    .setFooter("do `" + prefix + " bank` to see your balance");
            event.getChannel().sendMessage(embed.build()).queue((result) -> result.delete().queueAfter(3, TimeUnit.SECONDS), Throwable::printStackTrace);
        }

        //xp
        long xpAdd = roll.nextInt(7) + 8;
        long curXp = ((Number) user.get("xp")).longValue();
        long curLvl = ((Number) user.get("level")).longValue();
        long xpToNextLvl = curLvl * 150;

        user.replace("xp", curXp + xpAdd);
        curXp = ((Number) user.get("xp")).longValue();
        if (xpToNextLvl <= curXp) {
            user.replace("level", curLvl + 1);
            user.replace("xp", xpToNextLvl - curXp);
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(new Color(0, 255, 0))
                    .setTitle(event.getAuthor().getName() + " Leveled Up!")
                    .addField("\uD83D\uDD3C", curLvl + 1 + "", true)
                    .setFooter("do `" + prefix + " level` to see your xp");
            event.getChannel().sendMessage(embed.build()).queue((result) -> result.delete().queueAfter(5, TimeUnit.SECONDS), Throwable::printStackTrace);
        }

        //magic
        if (user.get("dungeon")!=null) {
            float magicAdd = (roll.nextInt(7) + 8)/100f;
            JSONObject dungeon = (JSONObject) user.get("dungeon");
            float curMagic = ((Number) dungeon.get("magic")).floatValue();

            dungeon.replace("magic", Float.valueOf(df.format(curMagic + magicAdd)));
        }


        //write the users to
        bot.users.writeContent();

        String messageContent = event.getMessage().getContentRaw();
        if (!messageContent.toLowerCase().startsWith(prefix)) return;

        messageContent = messageContent.substring(prefix.length());

        String[] args = messageContent.split(" ");
        String commandName = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        for (Command command : commandSet) {
            CommandBuilder builder = command.getClass().getAnnotation(CommandBuilder.class);
            if (!builder.name().equals(commandName) && !Arrays.asList(builder.aliases()).contains(commandName)) continue;

            try {
                command.run(new CommandEvent(event, args, this));
            } catch (Exception e) {
                event.getChannel().sendMessage("That is incorrect usage, the correct usage would be `" + prefix+getUsages(builder) + "`").queue((result) -> result.delete().queueAfter(5, TimeUnit.SECONDS), Throwable::printStackTrace);
            }
        }
    }

    public void onTextChannelCreate(TextChannelCreateEvent event) {
        event.getChannel().sendMessage(newChannel[random.nextInt(newChannel.length)]).queue();
    }

    public Set<Command> getCommands() {
        return commandSet;
    }

    public String getPrefix() {
        return prefix;
    }

    public Bot getBot() {
        return bot;
    }

    public DataBase findJSON(String s) {
        return new DataBase(bot.getDataLocation()+s);
    }

    public static String getUsages(CommandBuilder command) {
        if (command.usage().equals("")) return command.name();

        return command.name()+" "+ command.usage();
    }

    public void setAttractor(User user, AttractInfo attractor) {
        this.attractors.put(user.getId(), attractor);
    }

    public AttractInfo getAttractor(User user) {
        return this.attractors.getOrDefault(user.getId(), null);
    }

    public void stopAttracting(User user) {
        this.attractors.remove(user.getId());
    }

    public JSONObject getUserData(User user) {
        return bot.users.getSection(user.getId());
    }

    public JSONObject getUserData(String userId) {
        return bot.users.getSection(userId);
    }

    public void giveUserItem(User user, String itemId) {
        if (bot.users.getSection(user.getId()).get("inv")==null) {
            bot.users.getSection(user.getId()).put("inv", new JSONObject());
        }

        if (((JSONObject) bot.users.getSection(user.getId()).get("inv")).containsKey(itemId)) {
            int amt = Integer.parseInt(((JSONObject) bot.users.getSection(user.getId()).get("inv")).get(itemId).toString());
            ((JSONObject) bot.users.getSection(user.getId()).get("inv")).replace(itemId, amt+1);
        } else {
            ((JSONObject) bot.users.getSection(user.getId()).get("inv")).put(itemId, 1);
        }
        bot.users.writeContent();
    }

    public void takeUserItem(User user, String itemId) {
        if (bot.users.getSection(user.getId()).get("inv")==null) {
            bot.users.getSection(user.getId()).put("inv", new JSONObject());
        }

        if (((JSONObject) bot.users.getSection(user.getId()).get("inv")).containsKey(itemId)) {
            int amt = Integer.parseInt(((JSONObject) bot.users.getSection(user.getId()).get("inv")).get(itemId).toString());
            if (amt<=1) {
                ((JSONObject) bot.users.getSection(user.getId()).get("inv")).remove(itemId);
            } else {
                ((JSONObject) bot.users.getSection(user.getId()).get("inv")).replace(itemId, amt-1);
            }
        } else {
            ((JSONObject) bot.users.getSection(user.getId()).get("inv")).put(itemId, 1);
        }
        bot.users.writeContent();
    }
}
