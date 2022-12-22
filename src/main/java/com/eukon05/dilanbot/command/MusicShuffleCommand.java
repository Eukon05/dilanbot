package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.MessageUtils;
import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.kcommando.internal.annotations.HandleSlash;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

import java.util.ArrayList;
import java.util.Collections;

public class MusicShuffleCommand extends AbstractMusicCommand {

    public MusicShuffleCommand(PlayerManager playerManager) {
        super(playerManager);
    }

    @HandleSlash(name = "shuffle", desc = "Shuffles the queue randomly", global = true)
    @Override
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            interaction.respondLater();
            Server server = getServer(interaction);
            ServerMusicManager manager = playerManager.getServerMusicManager(server.getId());
            InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();
            String localeCode = interaction.getLocale().getLocaleCode();

            ArrayList<AudioTrack> queue = manager.getScheduler().getQueue();

            if (!voiceCheck(interaction))
                return;

            if (queue.isEmpty()) {
                responder.setContent(MessageUtils.getMessage("QUEUE_EMPTY", localeCode)).send();
                return;
            }

            Collections.shuffle(queue);

            responder.setContent(MessageUtils.getMessage("QUEUE_SHUFFLED", localeCode)).send();
        }).start();
    }

}
