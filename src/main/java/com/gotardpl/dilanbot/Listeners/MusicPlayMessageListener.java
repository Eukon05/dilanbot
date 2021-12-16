package com.gotardpl.dilanbot.Listeners;

import com.gotardpl.dilanbot.DTOs.ServerDTO;
import com.gotardpl.dilanbot.Lavaplayer.PlayerManager;
import com.gotardpl.dilanbot.Lavaplayer.ServerMusicManager;
import com.gotardpl.dilanbot.Services.ServerService;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicPlayMessageListener implements MessageCreateListener {

    private final ServerService serverService;
    private final PlayerManager playerManager;
    private final String keyWord = " play";


    @Autowired
    public MusicPlayMessageListener(ServerService serverService, PlayerManager playerManager){
        this.serverService=serverService;
        this.playerManager=playerManager;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        ServerDTO serverDTO = serverService.getServerById(event.getServer().get().getId());
        String message = event.getMessageContent();
        ServerTextChannel channel = event.getServerTextChannel().get();
        User me = event.getApi().getYourself();


        ServerMusicManager manager = playerManager.getServerMusicManager(serverDTO.getId());

        if(!message.startsWith(serverDTO.getPrefix() + keyWord))
            return;

        String song = message.replaceFirst(serverDTO.getPrefix() + keyWord, "").trim();

        if(song.isEmpty()) {
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

        if (!song.contains("http") && !song.contains("://")) {
            song = song.trim();
            song = "ytsearch:" + song;
        }

        else
            song = song.trim();

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

        playerManager.loadAndPlay(channel, song);


    }

}
