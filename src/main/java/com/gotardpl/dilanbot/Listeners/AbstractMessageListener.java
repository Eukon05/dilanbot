package com.gotardpl.dilanbot.Listeners;

import com.gotardpl.dilanbot.DTOs.ServerDTO;
import com.gotardpl.dilanbot.Services.ServerService;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractMessageListener implements MessageCreateListener {

    @Value("${prefix}")
    private String defaultPrefix;

    final String keyWord;
    ServerService serverService;
    ServerDTO serverDTO;
    String value;
    ServerTextChannel channel;
    private boolean allowDefaultPrefix = false;

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
            if(!allowDefaultPrefix || !message.startsWith(defaultPrefix + keyWord))
                return;
            else
                value = message.replaceFirst(defaultPrefix + keyWord,"").trim();
        }
        else
            value = message.replaceFirst(serverDTO.getPrefix() + keyWord,"").trim();

        channel = event.getServerTextChannel().get();


        childOnMessageCreate(event);

    }

    //Thanks to StackOverflow user Joakim Danielson for coming up with a great idea of moving the listeners main code
    //to another abstract method, instead of overriding onMessageCreate
    //https://bit.ly/3p3zGrE
    abstract void childOnMessageCreate(MessageCreateEvent event);

    public void setAllowDefaultPrefix(boolean allowDefaultPrefix){
        this.allowDefaultPrefix=allowDefaultPrefix;
    }
}
