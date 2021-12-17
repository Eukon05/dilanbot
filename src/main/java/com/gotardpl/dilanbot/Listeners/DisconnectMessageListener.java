package com.gotardpl.dilanbot.Listeners;

import com.gotardpl.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DisconnectMessageListener extends AbstractMusicMessageListener {


    @Autowired
    public DisconnectMessageListener(){
        super(" disconnect");
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

        //Here the track is stopped and the queue is cleared, I might change this behaviour in the future based on user feedback
        manager.player.stopTrack();
        manager.scheduler.clearQueue();

        playerManager.getServerAudioConnection(serverDTO.getId()).close();
        playerManager.removeServerAudioConnection(serverDTO.getId());

        channel.sendMessage("**Disconnected from "+ me.getConnectedVoiceChannel(event.getServer().get()).get().getName() +" **");

    }
}
