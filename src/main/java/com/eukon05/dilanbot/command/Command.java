package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.domain.DiscordServer;
import lombok.RequiredArgsConstructor;
import org.javacord.api.event.message.MessageCreateEvent;

@RequiredArgsConstructor
public abstract class Command {
    private final CommandMap commandMap;

    protected void addToCommands(String prefix) {
        commandMap.getCommands().put(prefix, this);
    }

    public abstract void run(MessageCreateEvent event, DiscordServer discordServer, String[] arguments);

    protected String fuseArguments(String[] arguments) {
        StringBuilder value = new StringBuilder();

        for (int i = 1; i < arguments.length; i++) {
            value.append(arguments[i]).append(" ");
        }

        return value.toString().trim();
    }
}
