package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.Message;
import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import me.koply.kcommando.internal.annotations.HandleSlash;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

public class MusicLoopCommand extends AbstractMusicCommand {

    public MusicLoopCommand(PlayerManager playerManager) {
        super(playerManager);
    }

    @HandleSlash(name = "loop", desc = "Enables or disables looping of the current track", global = true)
    @Override
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            interaction.respondLater();
            ServerMusicManager manager = playerManager.getServerMusicManager(getServer(interaction).getId());
            InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();
            String localeCode = interaction.getLocale().getLocaleCode();

            if (!comboCheck(interaction, manager))
                return;

            if (manager.getScheduler().getLoopTrack() == null) {
                manager.getScheduler().setLoopTrack(manager.getPlayer().getPlayingTrack());
                responder
                        .setContent(String.format(Message.LOOP_ENABLED.get(localeCode), manager.getScheduler().getLoopTrack().getInfo().title))
                        .send();
            } else {
                manager.getScheduler().setLoopTrack(null);
                responder.setContent(Message.LOOP_DISABLED.get(localeCode)).send();
            }
        }).start();
    }

}
