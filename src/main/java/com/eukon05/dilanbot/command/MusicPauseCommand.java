package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.Message;
import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.koply.kcommando.internal.annotations.HandleSlash;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

public class MusicPauseCommand extends AbstractMusicCommand {

    public MusicPauseCommand(PlayerManager playerManager) {
        super(playerManager);
    }

    @HandleSlash(name = "pause", desc = "Pauses the track", global = true)
    @Override
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            interaction.respondLater();
            ServerMusicManager manager = playerManager.getServerMusicManager(getServer(interaction).getId());
            InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();
            String localeCode = interaction.getLocale().getLocaleCode();

            AudioPlayer player = manager.getPlayer();

            if (!comboCheck(interaction, manager))
                return;

            if (player.isPaused()) {
                responder.setContent(Message.IS_PAUSED.get(localeCode)).send();
                return;
            }

            player.setPaused(true);
            responder.setContent(Message.PAUSED.get(localeCode)).send();
        }).start();
    }

}
