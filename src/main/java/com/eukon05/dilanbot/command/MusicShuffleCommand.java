package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import me.koply.kcommando.internal.annotations.HandleSlash;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

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

            if (!voiceCheck(interaction))
                return;

            int queueSize = manager.getScheduler().getQueue().size();

            if (queueSize == 0) {
                responder.setContent("**The queue is empty!**").send();
                return;
            }

            Collections.shuffle(manager.getScheduler().getQueue());

            responder.setContent("**:twisted_rightwards_arrows: Shuffled the queue**").send();
        }).start();
    }

}
