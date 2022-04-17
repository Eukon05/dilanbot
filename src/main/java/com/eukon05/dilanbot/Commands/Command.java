package com.eukon05.dilanbot.Commands;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import lombok.RequiredArgsConstructor;
import org.javacord.api.event.message.MessageCreateEvent;

@RequiredArgsConstructor
public abstract class Command{
    private final CommandMap commandMap;

    protected void addToCommands(String prefix){
        commandMap.commands.put(prefix, this);
    }
    abstract public void run(MessageCreateEvent event, ServerDTO serverDTO, String[] arguments);

    protected String fuseArguments(String[] arguments){
        StringBuilder value = new StringBuilder();

        for(int i = 1; i<arguments.length; i++){
            value.append(arguments[i]).append(" ");
        }

        return value.toString().trim();
    }
}
