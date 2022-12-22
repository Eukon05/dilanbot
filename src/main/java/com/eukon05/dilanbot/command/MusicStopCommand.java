package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.MessageUtils;
import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import me.koply.kcommando.internal.annotations.HandleSlash;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;

public class MusicStopCommand extends AbstractMusicCommand {


    public MusicStopCommand(PlayerManager playerManager) {
        super(playerManager);
    }

    @HandleSlash(name = "stop", desc = "Stops the music and clears the queue", global = true)
    @Override
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            interaction.respondLater();
            ServerMusicManager manager = playerManager.getServerMusicManager(getServer(interaction).getId());
            String localeCode = interaction.getLocale().getLocaleCode();

            if (!comboCheck(interaction, manager))
                return;

            manager.getPlayer().stopTrack();
            manager.getScheduler().setLoopTrack(null);
            manager.getPlayer().setPaused(false);
            manager.getScheduler().clearQueue();
            interaction.createFollowupMessageBuilder().setContent(MessageUtils.getMessage("STOPPED", localeCode)).send();
        }).start();
    }

}
