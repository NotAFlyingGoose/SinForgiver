package com.runningmanstudios.sinforgiver.commands.dungeon;

import com.runningmanstudios.sinforgiver.events.EventDetector;
import net.dv8tion.jda.api.entities.User;
import org.json.simple.JSONObject;

public class Fighter {
    private final String name;
    private final float rank;
    private final float magic;
    public Fighter(String name, float rank, float magic) {
        this.name = name;
        this.rank = rank;
        this.magic = magic;
    }

    public String getName() {
        return name;
    }

    public float getRank() {
        return rank;
    }

    public float getMagic() {
        return magic;
    }

    public static Fighter createFromUser(User user, EventDetector eventDetector) {
        JSONObject userData = eventDetector.getUserData(user);
        JSONObject dungeonData = (JSONObject) userData.get("dungeon");
        return new Fighter(user.getName(), ((Number)dungeonData.get("rank")).floatValue(), ((Number)dungeonData.get("magic")).floatValue());
    }
}
