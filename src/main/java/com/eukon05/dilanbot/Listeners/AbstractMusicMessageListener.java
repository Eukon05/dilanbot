package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.PlayerManager;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMusicMessageListener extends AbstractMessageListener{

    PlayerManager playerManager;

    public AbstractMusicMessageListener(String keyWord) {
        super(keyWord);
    }

    @Autowired
    private void setPlayerManager(PlayerManager playerManager){
        this.playerManager=playerManager;
    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event, ServerDTO serverDTO, String value) {
        User me = event.getApi().getYourself();
        ServerMusicManager manager = playerManager.getServerMusicManager(event.getServer().get().getId());
        childOnMessageCreate(event, serverDTO, value, me, manager);
    }

    abstract void childOnMessageCreate(MessageCreateEvent event, ServerDTO serverDTO, String value, User me, ServerMusicManager manager);
}
