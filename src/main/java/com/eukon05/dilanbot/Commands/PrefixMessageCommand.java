package com.eukon05.dilanbot.Commands;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Services.ServerService;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrefixMessageCommand extends Command {

    private final ServerService serverService;

    @Autowired
    public PrefixMessageCommand(ServerService serverService, CommandMap commandMap){
        super(commandMap);
        this.serverService=serverService;
        addToCommands("prefix");
    }

    @Override
    public void run(MessageCreateEvent event, ServerDTO serverDTO, String[] arguments) {

        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            if(arguments.length==1){
                channel.sendMessage("My current prefix is \"" +serverDTO.getPrefix()+"\", to change it, type: \n" +
                        serverDTO.getPrefix()+" prefix [new prefix here]");
                return;
            }

            serverDTO.setPrefix(arguments[1]);
            serverService.updateServer(serverDTO);

            channel.sendMessage("Done, my prefix will be \"" + arguments[1] + "\" from now on!");

        }).start();

    }
}
