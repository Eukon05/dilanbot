package com.gotardpl.dilanbot.Listeners;

import com.gotardpl.dilanbot.DTOs.ServerDTO;
import com.gotardpl.dilanbot.Services.ServerService;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMessageListener implements MessageCreateListener {

    final String keyWord;
    ServerService serverService;
    ServerDTO serverDTO;
    String value;
    ServerTextChannel channel;

    public AbstractMessageListener(String keyWord){
        this.keyWord=keyWord;
    }

    @Autowired
    private void setServerService(ServerService serverService){
        this.serverService=serverService;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        serverDTO = serverService.getServerById(event.getServer().get().getId());
        String message = event.getMessageContent();

        if(!message.startsWith(serverDTO.getPrefix() + keyWord)) {
            return;
        }

        channel = event.getServerTextChannel().get();
        value = message.replaceFirst(serverDTO.getPrefix() + keyWord,"").trim();

        childOnMessageCreate(event);

    }

    //Thanks to StackOverflow user Joakim Danielson for coming up with a great idea of moving the listeners main code
    //to another abstract method, instead of overriding onMessageCreate
    //https://bit.ly/3p3zGrE
    abstract void childOnMessageCreate(MessageCreateEvent event);

}
