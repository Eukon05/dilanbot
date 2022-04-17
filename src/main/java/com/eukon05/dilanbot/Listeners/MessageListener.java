package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.Commands.Command;
import com.eukon05.dilanbot.Commands.CommandMap;
import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Services.ServerService;
import lombok.RequiredArgsConstructor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public  class MessageListener implements MessageCreateListener {

    private final ServerService serverService;
    private final CommandMap commandMap;

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        if(event.getMessageAuthor().isYourself() || event.getMessage().isPrivateMessage() || event.getMessageAuthor().isBotUser())
            return;

        ServerDTO serverDTO = serverService.getServerById(event.getServer().get().getId());
        String message = event.getMessageContent();

        if(!message.startsWith(serverDTO.getPrefix()))
            return;

        String[] arguments = message.replace(serverDTO.getPrefix(), "").trim().split(" ");

        if(!commandMap.commands.containsKey(arguments[0]))
            return;

        Command command = commandMap.commands.get(arguments[0]);
        command.run(event, serverDTO, arguments);

    }

}
