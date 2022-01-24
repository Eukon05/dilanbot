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

        Thread thread = new Thread(){

            @Override
            public void run(){

                ServerTextChannel channel = event.getServerTextChannel().get();

                if(me.getConnectedVoiceChannel(event.getServer().get()).isEmpty()){
                    channel.sendMessage("I'm not connected to a voice channel!");
                    return;
                }

                if(event.getMessageAuthor().getConnectedVoiceChannel().isEmpty() ||
                        !(me.getConnectedVoiceChannel(channel.getServer()).get() == event.getMessageAuthor().getConnectedVoiceChannel().get())) {
                    channel.sendMessage("You have to be in the same channel as me!");
                    return;
                }

                int queueSize = manager.scheduler.getQueue().size();

                if(queueSize==0){
                    channel.sendMessage("**The queue is empty!**");
                    return;
                }

                manager.scheduler.clearQueue();
                channel.sendMessage("**Cleared " + queueSize + " tracks from the queue!**");

            }

        };

        thread.start();

    }

}
