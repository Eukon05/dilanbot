package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import me.koply.kcommando.internal.annotations.HandleSlash;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;

public class DisconnectCommand extends AbstractMusicCommand {

    public DisconnectCommand(PlayerManager playerManager) {
        super(playerManager);
    }

    @HandleSlash(name = "disconnect", desc = "Disconnects the bot from a voice channel (if it's connected to one)", global = true)
    @Override
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            interaction.respondLater();
            ServerMusicManager manager = playerManager.getServerMusicManager(getServer(interaction).getId());

            Server server = getServer(interaction);
            User me = getSelf(interaction);

            if (!voiceCheck(interaction))
                return;

            //Here the track is stopped and the queue is cleared, I might change this behaviour in the future based on user feedback
            manager.getPlayer().stopTrack();
            manager.getScheduler().clearQueue();

            playerManager.getServerAudioConnection(server.getId()).close();
            playerManager.removeServerAudioConnection(server.getId());

            interaction.createImmediateResponder().setContent("**Disconnected from " + me.getConnectedVoiceChannel(server).get().getName() + " **").respond();

        }).start();
    }
}
