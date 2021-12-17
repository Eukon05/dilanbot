package com.gotardpl.dilanbot.Listeners;

import com.gotardpl.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicStopMessageListener extends AbstractMusicMessageListener {

    @Autowired
    public MusicStopMessageListener(){
        super(" stop");
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        super.onMessageCreate(event);
    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event) {

        if(me.getConnectedVoiceChannel(event.getServer().get()).isEmpty()){
            channel.sendMessage("I'm not connected to a voice channel!");
            return;
        }

        if(!(me.getConnectedVoiceChannel(channel.getServer()).get() == event.getMessageAuthor().getConnectedVoiceChannel().get())){
            channel.sendMessage("You have to be in the same channel as me!");
            return;
        }

        ServerMusicManager manager = playerManager.getServerMusicManager(serverDTO.getId());

        if(manager.player.getPlayingTrack()==null) {
            channel.sendMessage("**:x: Nothing is playing right now**");
            return;
        }

        manager.player.stopTrack();
        manager.scheduler.clearQueue();
        channel.sendMessage("**:no_entry: Music stopped**");

    }

}
