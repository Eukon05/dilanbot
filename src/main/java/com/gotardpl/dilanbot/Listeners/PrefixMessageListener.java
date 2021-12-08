package com.gotardpl.dilanbot.Listeners;

import com.gotardpl.dilanbot.DTOs.ServerDTO;
import com.gotardpl.dilanbot.Services.ServerService;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrefixMessageListener implements MessageCreateListener {

    private final ServerService serverService;
    private final String keyWord = " prefix";

    @Autowired
    public PrefixMessageListener(ServerService serverService){
        this.serverService=serverService;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        //I'm using Optional.get() without checks because they will always return an object, since we are retrieving a message from a channel on a server

        ServerDTO serverDTO = serverService.getServerById(event.getServer().get().getId());
        String message = event.getMessageContent();
        ServerTextChannel channel = event.getServerTextChannel().get();

        if(!message.startsWith(serverDTO.getPrefix() + keyWord))
            return;

        String newPrefix = message.replaceFirst(serverDTO.getPrefix() + keyWord,"").trim();

        if(newPrefix.isEmpty()){
            channel.sendMessage("My current prefix is \"" +serverDTO.getPrefix()+"\", to change it, type: \n" +
                    serverDTO.getPrefix()+" prefix [new prefix here]");
            return;
        }

        serverDTO.setPrefix(newPrefix);
        serverService.updateServer(serverDTO);

        channel.sendMessage("Done, my prefix will be \"" + newPrefix + "\" from now on!");

    }

}
