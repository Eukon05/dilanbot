package com.gotardpl.dilanbot.Listeners;

import com.gotardpl.dilanbot.Lavaplayer.PlayerManager;
import com.gotardpl.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMusicMessageListener extends AbstractMessageListener{

    PlayerManager playerManager;
    ServerMusicManager manager;
    User me;

    public AbstractMusicMessageListener(String keyWord) {
        super(keyWord);
    }

    @Autowired
    private void setPlayerManager(PlayerManager playerManager){
        this.playerManager=playerManager;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        super.onMessageCreate(event);
        if(!isCorrectListener)
            return;

        me = event.getApi().getYourself();
        manager = playerManager.getServerMusicManager(serverDTO.getId());
    }
}
