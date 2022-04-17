package com.eukon05.dilanbot.Commands;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicNowPlayingCommand extends MusicCommand {

    @Autowired
    public MusicNowPlayingCommand(CommandMap commandMap) {
        super(commandMap);
        addToCommands("np");
    }


    @Override
    public void run(MessageCreateEvent event, ServerDTO serverDTO, String[] arguments, User me, ServerMusicManager manager) {
        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            if(!comboCheck(me, event, manager))
                return;

            AudioTrack track = manager.player.getPlayingTrack();

            new MessageBuilder().setEmbed(new EmbedBuilder()
                            .setTitle("Now Playing")
                            .setDescription("["+track.getInfo().title+"]("+track.getInfo().uri+")")
                            .setThumbnail(track.getInfo().artworkUrl))
                    .send(channel);

        }).start();
    }
}
