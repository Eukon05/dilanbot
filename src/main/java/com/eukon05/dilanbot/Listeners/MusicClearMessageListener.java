package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class MusicClearMessageListener extends AbstractMusicMessageListener {

    public MusicClearMessageListener(){
        super(" clear");

    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event, ServerDTO serverDTO, String value, User me, ServerMusicManager manager) {

        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            if(!isBotOnVCCheck(me, event) || !isUserOnVCCheck(me, event))
                return;

            int queueSize = manager.scheduler.getQueue().size();

            if(queueSize==0){
                channel.sendMessage("**The queue is empty!**");
                return;
            }

            manager.scheduler.clearQueue();
            channel.sendMessage("**Cleared " + queueSize + " tracks from the queue!**");

        }).start();

    }

}
