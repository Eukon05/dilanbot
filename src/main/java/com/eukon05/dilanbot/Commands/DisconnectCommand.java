package com.eukon05.dilanbot.Commands;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DisconnectCommand extends MusicCommand {
    @Autowired
    public DisconnectCommand(CommandMap commandMap){
        super(commandMap);
        addToCommands("disconnect");
    }

    @Override
    public void run(MessageCreateEvent event, ServerDTO serverDTO, String[] arguments, User me, ServerMusicManager manager) {

        new Thread(() -> {

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

            //Here the track is stopped and the queue is cleared, I might change this behaviour in the future based on user feedback
            manager.player.stopTrack();
            manager.scheduler.clearQueue();

            playerManager.getServerAudioConnection(serverDTO.getId()).close();
            playerManager.removeServerAudioConnection(serverDTO.getId());

            channel.sendMessage("**Disconnected from "+ me.getConnectedVoiceChannel(event.getServer().get()).get().getName() +" **");

        }).start();

    }
}
