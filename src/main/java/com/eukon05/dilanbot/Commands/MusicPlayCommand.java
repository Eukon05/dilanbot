package com.eukon05.dilanbot.Commands;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicPlayCommand extends MusicCommand {

    @Autowired
    public MusicPlayCommand(CommandMap commandMap){
        super(commandMap);
        addToCommands("play");
    }

    @Override
    public void run(MessageCreateEvent event, ServerDTO serverDTO, String[] arguments, User me, ServerMusicManager manager) {

        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();
            String value = fuseArguments(arguments);

            if (value.isEmpty()) {

                if (!comboCheck(me, event, manager))
                    return;

                if (manager.player.isPaused()) {
                    manager.player.setPaused(false);
                    channel.sendMessage("**:arrow_forward: Music resumed**");
                } else
                    channel.sendMessage("**:x: The track isn't paused!**");

                return;
            }

            if (!value.startsWith("http://") && !value.startsWith("https://")) {
                value = "ytsearch:" + value;
            }

            if (me.getConnectedVoiceChannel(channel.getServer()).isEmpty()) {

                if (event.getMessageAuthor().getConnectedVoiceChannel().isEmpty()) {
                    channel.sendMessage("**:x: You have to be connected to a voice channel!**");
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

            } else if (!isUserOnVCCheck(me, event))
                return;

            playerManager.loadAndPlay(channel, value);

        }).start();

    }

}
