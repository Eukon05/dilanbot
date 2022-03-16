package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class MusicSkipMessageListener extends AbstractMusicMessageListener {

    public MusicSkipMessageListener(){
        super(" skip");
    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event, ServerDTO serverDTO, String value, User me, ServerMusicManager manager) {

        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            if(!comboCheck(me, event, manager))
                return;

            if(manager.scheduler.loopTrack!=null){
                manager.scheduler.loopTrack=null;
                channel.sendMessage("**:warning: Loop disabled!**");
            }

            manager.scheduler.nextTrack();
            channel.sendMessage("**:fast_forward: Skipped**");

        }).start();

    }

}
