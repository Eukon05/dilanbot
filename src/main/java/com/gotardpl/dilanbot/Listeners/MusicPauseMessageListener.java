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
public class MusicPauseMessageListener implements MessageCreateListener {

    private final ServerService serverService;
    private final PlayerManager playerManager;
    private final String keyWord = " pause";

    @Autowired
    public MusicPauseMessageListener(ServerService serverService, PlayerManager playerManager){
        this.serverService=serverService;
        this.playerManager=playerManager;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        ServerDTO serverDTO = serverService.getServerById(event.getServer().get().getId());
        String message = event.getMessageContent();
        ServerTextChannel channel = event.getServerTextChannel().get();
        User me = event.getApi().getYourself();

        if(!message.startsWith(serverDTO.getPrefix() + keyWord))
            return;

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

        if(manager.player.isPaused()){
            channel.sendMessage("**:x: The player is already paused!**");
            return;
        }

        manager.player.setPaused(true);
        channel.sendMessage("**:pause_button: Music paused**");


    }
}
