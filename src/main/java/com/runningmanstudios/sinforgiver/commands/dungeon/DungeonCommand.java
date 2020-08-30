package com.runningmanstudios.sinforgiver.commands.dungeon;

import com.runningmanstudios.sinforgiver.commandUtil.AttractInfo;
import com.runningmanstudios.sinforgiver.commandUtil.AttractableCommand;
import com.runningmanstudios.sinforgiver.commandUtil.CommandBuilder;
import com.runningmanstudios.sinforgiver.events.CommandEvent;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.regex.Pattern;

@CommandBuilder(name = "dungeon", description = "travel through the darkest depths of a really cool dungeon. you can do add the word `restart` as a parameter to restart your game", usage = "restart?", aliases = {"game"})
public class DungeonCommand implements AttractableCommand {
    Map<String, Dungeon> games = new HashMap<>();
    List<String> restartRequests = new LinkedList<>();

    @Override
    public void onAttract(CommandEvent command) {
        if (command.getMessage().getContentRaw().equals("Restart My Game ("+command.getAuthor().getId()+")") && restartRequests.contains(command.getAuthor().getId())) {
            JSONObject userData = command.getEventDetector().getUserData(command.getAuthor());
            userData.remove("dungeon");
            command.getEventDetector().getBot().users.writeContent();
            restartRequests.remove(command.getAuthor().getId());
            command.reply("Your Game was removed from the system").queue();
            return;
        }

        boolean skip = false;
        if (!games.containsKey(command.getAuthor().getId())) {
            games.put(command.getAuthor().getId(), new Dungeon(command.getAuthor(), command));
            skip=true;
        }

        if (skip) {
            AttractInfo attract = new AttractInfo(this);
            for (String pattern : games.get(command.getAuthor().getId()).getNextPatterns()) {
                attract.addAnswer(Pattern.compile(pattern));
            }
            command.getEventDetector().setAttractor(command.getAuthor(), attract);
            return;
        }

        games.get(command.getAuthor().getId()).onResponse(command);
        AttractInfo attract = new AttractInfo(this);
        for (String pattern : games.get(command.getAuthor().getId()).getNextPatterns()) {
            attract.addAnswer(Pattern.compile(pattern));
        }
        command.getEventDetector().setAttractor(command.getAuthor(), attract);
    }

    @Override
    public void run(CommandEvent command) {
        if (command.getArgs().length!=0 && command.getArg(0).equals("restart")) {
            games.remove(command.getAuthor().getId());
            command.reply("Are you sure you want to restart your game, there is no reversing this? (Type `Restart My Game ("+command.getAuthor().getId()+")` to confirm)").queue();
            command.getEventDetector().setAttractor(command.getAuthor(), new AttractInfo(this).addAnswer(Pattern.compile("Restart My Game \\("+command.getAuthor().getId()+"\\)")));
            restartRequests.add(command.getAuthor().getId());
            return;
        }

        if (!games.containsKey(command.getAuthor().getId()) || !games.get(command.getAuthor().getId()).running) {
            command.reply("Would you like to start/continue a game? (Y/N)").queue();
            command.getEventDetector().setAttractor(command.getAuthor(), new AttractInfo(this).addAnswer(Pattern.compile("Y")));
            return;
        }

        command.reply(games.get(command.getAuthor().getId()).getLastShow() + "\nPlease answer with one of the following: " + games.get(command.getAuthor().getId()).getNextPatterns()).queue();
        AttractInfo attract = new AttractInfo(this);
        for (String pattern : games.get(command.getAuthor().getId()).getNextPatterns()) {
            attract.addAnswer(Pattern.compile(pattern));
        }
        command.getEventDetector().setAttractor(command.getAuthor(), attract);
    }
}
