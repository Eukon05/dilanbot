package com.gotardpl.dilanbot.Listeners;

import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicPlayMessageListener extends AbstractMusicMessageListener {

    public MusicPlayMessageListener(){
        super(" play");
    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event) {

        if(value.isEmpty()) {
            if (manager.player.isPaused()) {
                manager.player.setPaused(false);
                channel.sendMessage("**:arrow_forward: Music resumed**");
            }
            else
                channel.sendMessage("**:x: The track isn't paused!**");

            return;
        }

        if(event.getMessageAuthor().getConnectedVoiceChannel().isEmpty()){
            channel.sendMessage("You have to be connected to a voice channel!");
            return;
        }

        if (!value.contains("http") && !value.contains("://")) {
            value = "ytsearch:" + value;
        }

        if(me.getConnectedVoiceChannel(channel.getServer()).isEmpty()){

            event.getMessageAuthor().getConnectedVoiceChannel().get().connect().thenAccept(audioConnection -> {

                audioConnection.setAudioSource(manager.audioSource);
                playerManager.addServerAudioConnection(serverDTO.getId(), audioConnection);

            }).exceptionally(e -> {
                channel.sendMessage("Something went wrong! " + e.getMessage());
                e.printStackTrace();
                return null;
            });

        }
        else
        if(!(me.getConnectedVoiceChannel(channel.getServer()).get() == event.getMessageAuthor().getConnectedVoiceChannel().get())){
            channel.sendMessage("You have to be in the same channel as me!");
            return;
        }

        playerManager.loadAndPlay(channel, value);

    }

}
