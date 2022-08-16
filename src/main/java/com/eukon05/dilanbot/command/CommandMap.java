package com.eukon05.dilanbot.command;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class CommandMap {
    private final Map<String, Command> commands = new HashMap<>();
}
