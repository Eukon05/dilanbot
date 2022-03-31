package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class MusicNowPlayingListener extends AbstractMusicMessageListener{

    public MusicNowPlayingListener() {
        super(" np");
    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event, ServerDTO serverDTO, String value, User me, ServerMusicManager manager) {

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
