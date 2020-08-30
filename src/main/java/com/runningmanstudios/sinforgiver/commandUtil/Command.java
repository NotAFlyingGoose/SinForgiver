package com.runningmanstudios.sinforgiver.commandUtil;

import com.runningmanstudios.sinforgiver.events.CommandEvent;

public interface Command {
    void run(CommandEvent event);
}
