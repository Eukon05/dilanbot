package com.gotardpl.dilanbot.Listeners;

import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicClearMessageListener extends AbstractMusicMessageListener {


    @Autowired
    public MusicClearMessageListener(){
        super(" clear");

    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        super.onMessageCreate(event);

        if(!isCorrectListener)
            return;

        if(me.getConnectedVoiceChannel(event.getServer().get()).isEmpty()){
            channel.sendMessage("I'm not connected to a voice channel!");
            return;
        }

        if(!(me.getConnectedVoiceChannel(channel.getServer()).get() == event.getMessageAuthor().getConnectedVoiceChannel().get())){
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

}
