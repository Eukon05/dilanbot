package com.gotardpl.dilanbot.Listeners;

import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicPauseMessageListener extends AbstractMusicMessageListener {

    @Autowired
    public MusicPauseMessageListener(){
        super(" pause");
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

        if(manager.player.getPlayingTrack()==null) {
            channel.sendMessage("**:x: Nothing is playing right now**");
            return;
        }

        if(manager.player.isPaused()){
            channel.sendMessage("**:x: The player is already paused!**");
            return;
        }

        manager.player.setPaused(true);
        channel.sendMessage("**:pause_button: Music paused**");


    }
}
