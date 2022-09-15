package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.repository.CommandRepository;
import org.javacord.api.event.message.MessageCreateEvent;

public abstract class Command {

    protected Command(String prefix, CommandRepository commandRepository){
        commandRepository.addCommand(prefix, this);
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
