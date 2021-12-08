package com.gotardpl.dilanbot.Listeners;

import com.gotardpl.dilanbot.Services.ServerService;
import org.javacord.api.event.server.ServerJoinEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerJoinListenerImpl implements org.javacord.api.listener.server.ServerJoinListener {

    private final ServerService serverService;

    @Autowired
    public ServerJoinListenerImpl(ServerService serverService){
        this.serverService=serverService;
    }

    @Override
    public void onServerJoin(ServerJoinEvent event) {

        if(serverService.getServerById(event.getServer().getId())==null)
            serverService.addServer(event.getServer().getId());

    }
}
