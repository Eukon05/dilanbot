package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.MessageUtils;
import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import me.koply.kcommando.internal.annotations.HandleSlash;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

public class MusicClearCommand extends AbstractMusicCommand {

    public MusicClearCommand(PlayerManager playerManager) {
        super(playerManager);
    }

    @HandleSlash(name = "clear", desc = "Clears the queue", global = true)
    @Override
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            interaction.respondLater();
            ServerMusicManager manager = playerManager.getServerMusicManager(getServer(interaction).getId());
            InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();
            String localeCode = interaction.getLocale().getLocaleCode();

            if (!voiceCheck(interaction))
                return;

            int queueSize = manager.getScheduler().getQueue().size();

            if (queueSize == 0) {
                responder.setContent(MessageUtils.getMessage("QUEUE_EMPTY", localeCode)).send();
                return;
            }

            manager.getScheduler().clearQueue();

            if (queueSize == 1)
                responder.setContent(MessageUtils.getMessage("QUEUE_REMOVED_TRACK", localeCode)).send();
            else
                responder.setContent(String.format(MessageUtils.getMessage("QUEUE_REMOVED_TRACKS", localeCode), queueSize)).send();
        }).start();
    }

}
