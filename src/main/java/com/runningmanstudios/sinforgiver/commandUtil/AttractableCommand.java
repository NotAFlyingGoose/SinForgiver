package com.runningmanstudios.sinforgiver.commandUtil;

import com.runningmanstudios.sinforgiver.events.CommandEvent;

public interface AttractableCommand extends Command {
    void onAttract(CommandEvent event);
}
