package com.eukon05.dilanbot.listener;

import com.eukon05.dilanbot.command.Command;
import com.eukon05.dilanbot.command.CommandMap;
import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.service.DiscordServerService;
import lombok.RequiredArgsConstructor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageListener implements MessageCreateListener {

    private final DiscordServerService discordServerService;
    private final CommandMap commandMap;

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        if (event.getMessageAuthor().isYourself() || event.getMessage().isPrivateMessage() || event.getMessageAuthor().isBotUser())
            return;

        DiscordServer discordServer = discordServerService.getServerById(event.getServer().get().getId());
        String message = event.getMessageContent();

        if (!message.startsWith(discordServer.getPrefix()))
            return;

        String[] arguments = message.replace(discordServer.getPrefix(), "").trim().split(" ");

        if (!commandMap.getCommands().containsKey(arguments[0]))
            return;

        Command command = commandMap.getCommands().get(arguments[0]);
        command.run(event, discordServer, arguments);

    }

}
