package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.Lavaplayer.PlayerManager;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
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
        me = event.getApi().getYourself();
        manager = playerManager.getServerMusicManager(event.getServer().get().getId()); //I'm using server id from event, since serverDTO is not yet instantiated
        super.onMessageCreate(event);
        //for some reason I was able to instantiate the manager object in *this* line, despite it being required in the above method
        //if someone knows why, please DM me on Discord, you can find my username on my GitHub
    }
}
