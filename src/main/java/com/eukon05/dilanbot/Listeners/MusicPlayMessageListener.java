package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class MusicPlayMessageListener extends AbstractMusicMessageListener {

    public MusicPlayMessageListener(){
        super(" play");
    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event, ServerDTO serverDTO, String value, User me, ServerMusicManager manager) {

        //This code is AWFUL. I need to extract the VC checks into a method in the AbstractMusicMessageListener class.

        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();
            String valueCopy = value; //Required, since variables accessed from inner classes have to be final, and we can't reassign the variable "value" directly

            if(valueCopy.isEmpty()) {

                if(me.getConnectedVoiceChannel(event.getServer().get()).isEmpty()){
                    channel.sendMessage("I'm not connected to a voice channel!");
                    return;
                }

                if(event.getMessageAuthor().getConnectedVoiceChannel().isEmpty() ||
                        !(me.getConnectedVoiceChannel(channel.getServer()).get() == event.getMessageAuthor().getConnectedVoiceChannel().get())) {
                    channel.sendMessage("You have to be in the same channel as me!");
                    return;
                }

                if(manager.player.getPlayingTrack()==null) {
                    channel.sendMessage("**:x: Nothing is playing right now**");
                    return;
                }

                if (manager.player.isPaused()) {
                    manager.player.setPaused(false);
                    channel.sendMessage("**:arrow_forward: Music resumed**");
                }
                else
                    channel.sendMessage("**:x: The track isn't paused!**");

                return;
            }

            if (!valueCopy.startsWith("http://") && !valueCopy.startsWith("https://")) {
                valueCopy = "ytsearch:" + valueCopy;
            }

            if(me.getConnectedVoiceChannel(channel.getServer()).isEmpty()){

                if(event.getMessageAuthor().getConnectedVoiceChannel().isEmpty()){
                    channel.sendMessage("You have to be connected to a voice channel!");
                    return;
                }

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
            if(event.getMessageAuthor().getConnectedVoiceChannel().isEmpty() ||
                    !(me.getConnectedVoiceChannel(channel.getServer()).get() == event.getMessageAuthor().getConnectedVoiceChannel().get())) {
                channel.sendMessage("You have to be in the same channel as me!");
                return;
            }

            playerManager.loadAndPlay(channel, valueCopy);

        }).start();

    }



}
