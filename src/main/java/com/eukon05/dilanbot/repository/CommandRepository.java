package com.eukon05.dilanbot.repository;

import com.eukon05.dilanbot.command.Command;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommandRepository {
    private final Map<String, Command> commands = new HashMap<>();

    public void addCommand(String prefix, Command command){
        commands.put(prefix, command);
    }

    public Command getCommand(String prefix){
        return commands.get(prefix);
    }

    public boolean commandExists(String prefix){
        return commands.containsKey(prefix);
    }
}
