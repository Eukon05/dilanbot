package com.eukon05.dilanbot.Commands;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CommandMap {
    public final HashMap<String, Command> commands = new HashMap<>();
}
