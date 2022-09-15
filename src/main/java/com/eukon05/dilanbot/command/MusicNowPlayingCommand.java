package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import com.eukon05.dilanbot.repository.CommandRepository;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class MusicNowPlayingCommand extends MusicCommand {

    public MusicNowPlayingCommand(CommandRepository commandRepository) {
        super("np", commandRepository);
    }


    @Override
    public void run(MessageCreateEvent event, DiscordServer discordServer, String[] arguments, User me, ServerMusicManager manager) {
        new Thread(() -> {
            ServerTextChannel channel = event.getServerTextChannel().get();

            if (!comboCheck(me, event, manager))
                return;

            AudioTrack track = manager.getPlayer().getPlayingTrack();

            new MessageBuilder().setEmbed(new EmbedBuilder()
                            .setTitle("Now Playing")
                            .setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")")
                            .setThumbnail(track.getInfo().artworkUrl))
                    .send(channel);
        }).start();
    }

}
