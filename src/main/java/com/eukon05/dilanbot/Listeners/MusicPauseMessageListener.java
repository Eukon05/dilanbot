package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class MusicPauseMessageListener extends AbstractMusicMessageListener {

    public MusicPauseMessageListener(){
        super(" pause");
    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event, ServerDTO serverDTO, String value, User me, ServerMusicManager manager) {

        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            if(!comboCheck(me, event, manager))
                return;

            if(manager.player.isPaused()){
                channel.sendMessage("**:x: The player is already paused!**");
                return;
            }

            manager.player.setPaused(true);
            channel.sendMessage("**:pause_button: Music paused**");

        }).start();

    }


}
