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
public class MusicSkipCommand extends MusicCommand {

    public MusicSkipCommand(CommandRepository commandRepository) {
        super("skip", commandRepository);
    }

    @Override
    public void run(MessageCreateEvent event, DiscordServer discordServer, String[] arguments, User me, ServerMusicManager manager) {
        new Thread(() -> {
            ServerTextChannel channel = event.getServerTextChannel().get();

            if (!comboCheck(me, event, manager))
                return;

            if (manager.getScheduler().getLoopTrack() != null) {
                manager.getScheduler().setLoopTrack(null);
                channel.sendMessage("**:warning: Loop disabled!**");
            }

            manager.getScheduler().nextTrack();
            channel.sendMessage("**:fast_forward: Skipped**");

            AudioTrack track = manager.getPlayer().getPlayingTrack();

            if (track == null)
                return;

            new MessageBuilder().setEmbed(new EmbedBuilder()
                            .setTitle("Now Playing")
                            .setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")")
                            .setThumbnail(track.getInfo().artworkUrl))
                    .send(channel);

        }).start();
    }

}
