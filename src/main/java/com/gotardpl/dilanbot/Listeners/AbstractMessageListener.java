package com.gotardpl.dilanbot.Listeners;

import com.gotardpl.dilanbot.DTOs.ServerDTO;
import com.gotardpl.dilanbot.Services.ServerService;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMessageListener implements MessageCreateListener {

    String keyWord;
    ServerService serverService;
    ServerDTO serverDTO;
    String value;
    ServerTextChannel channel;
    boolean isCorrectListener = true;

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
            isCorrectListener = false;
            return;
        }

        channel = event.getServerTextChannel().get();
        value = message.replaceFirst(serverDTO.getPrefix() + keyWord,"").trim();

    }

}
