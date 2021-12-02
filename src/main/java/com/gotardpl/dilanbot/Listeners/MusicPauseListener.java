package com.gotardpl.dilanbot.Listeners;

import com.gotardpl.dilanbot.DTOs.ServerDTO;
import com.gotardpl.dilanbot.Lavaplayer.PlayerManager;
import com.gotardpl.dilanbot.Lavaplayer.ServerMusicManager;
import com.gotardpl.dilanbot.Services.ServerService;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicPauseListener implements MessageCreateListener {

    private final ServerService serverService;
    private final PlayerManager playerManager;
    private final String keyWord = " pause";

    @Autowired
    public MusicPauseListener(ServerService serverService, PlayerManager playerManager){
        this.serverService=serverService;
        this.playerManager=playerManager;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {

        ServerDTO serverDTO = serverService.getServerById(messageCreateEvent.getServer().get().getId());
        String message = messageCreateEvent.getMessageContent();
        ServerTextChannel channel = messageCreateEvent.getServerTextChannel().get();
        DiscordApi api = messageCreateEvent.getApi();

        if(!message.startsWith(serverDTO.getPrefix() + keyWord))
            return;

        if(!(api.getYourself().getConnectedVoiceChannel(channel.getServer()).get() == messageCreateEvent.getMessageAuthor().getConnectedVoiceChannel().get())){
            channel.sendMessage("You have to be in the same channel as me!");
            return;
        }

        ServerMusicManager manager = playerManager.getServerMusicManager(serverDTO.getId());

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
