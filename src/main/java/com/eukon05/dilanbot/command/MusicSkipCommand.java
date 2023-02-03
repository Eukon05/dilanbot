package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.Message;
import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.kcommando.internal.annotations.HandleSlash;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

import static com.eukon05.dilanbot.Message.MARKDOWN_URL;

public class MusicSkipCommand extends AbstractMusicCommand {

    public MusicSkipCommand(PlayerManager playerManager) {
        super(playerManager);
    }

    @HandleSlash(name = "skip", desc = "Skips the currently playing track", global = true)
    @Override
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            interaction.respondLater();
            Server server = getServer(interaction);
            ServerMusicManager manager = playerManager.getServerMusicManager(server.getId());
            InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();
            String localeCode = interaction.getLocale().getLocaleCode();

            if (!comboCheck(interaction, manager))
                return;

            if (manager.getScheduler().getLoopTrack() != null) {
                manager.getScheduler().setLoopTrack(null);
                responder.setContent(Message.LOOP_DISABLED.get(localeCode)).send();
            }

            manager.getScheduler().nextTrack();
            responder.setContent(Message.SKIPPED.get(localeCode)).send();

            AudioTrack track = manager.getPlayer().getPlayingTrack();

            if (track == null)
                return;

            interaction.createFollowupMessageBuilder().addEmbed(new EmbedBuilder()
                            .setTitle(Message.NP.get(localeCode))
                            .setDescription(String.format(MARKDOWN_URL, track.getInfo().title, track.getInfo().uri))
                            .setThumbnail(track.getInfo().artworkUrl))
                    .send();
        }).start();
    }

}
