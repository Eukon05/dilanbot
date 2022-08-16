package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.service.DiscordServerService;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class PrefixMessageCommand extends Command {

    private final DiscordServerService discordServerService;

    public PrefixMessageCommand(DiscordServerService discordServerService, CommandMap commandMap) {
        super(commandMap);
        this.discordServerService = discordServerService;
        addToCommands("prefix");
    }

    @Override
    public void run(MessageCreateEvent event, DiscordServer discordServer, String[] arguments) {

        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            if (arguments.length == 1) {
                channel.sendMessage("My current prefix is \"" + discordServer.getPrefix() + "\", to change it, type: \n" +
                        discordServer.getPrefix() + " prefix [new prefix here]");
                return;
            }

            discordServer.setPrefix(arguments[1]);
            discordServerService.updateServer(discordServer);

            channel.sendMessage("Done, my prefix will be \"" + arguments[1] + "\" from now on!");

        }).start();

    }
}
