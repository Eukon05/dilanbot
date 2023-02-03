package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.Message;
import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.kcommando.internal.annotations.HandleSlash;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;

import static com.eukon05.dilanbot.Message.MARKDOWN_URL;

public class MusicNowPlayingCommand extends AbstractMusicCommand {

    public MusicNowPlayingCommand(PlayerManager playerManager) {
        super(playerManager);
    }

    @HandleSlash(name = "np", desc = "Shows the currently playing track", global = true)
    @Override
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            interaction.respondLater();
            ServerMusicManager manager = playerManager.getServerMusicManager(getServer(interaction).getId());
            String localeCode = interaction.getLocale().getLocaleCode();

            if (!comboCheck(interaction, manager))
                return;

            AudioTrack track = manager.getPlayer().getPlayingTrack();

            interaction.createFollowupMessageBuilder().addEmbed(new EmbedBuilder()
                            .setTitle(Message.NP.get(localeCode))
                            .setDescription(String.format(MARKDOWN_URL, track.getInfo().title, track.getInfo().uri))
                            .setThumbnail(track.getInfo().artworkUrl))
                    .send();
        }).start();
    }

}
