package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.Message;
import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import me.koply.kcommando.internal.OptionType;
import me.koply.kcommando.internal.annotations.HandleSlash;
import me.koply.kcommando.internal.annotations.Option;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

import java.util.Optional;

public class MusicPlayCommand extends AbstractMusicCommand {

    public MusicPlayCommand(PlayerManager playerManager) {
        super(playerManager);
    }

    @HandleSlash(name = "play",
            desc = "Plays the specified track or disables pause",
            options = @Option(name = "title", desc = "Name of the song to play", type = OptionType.STRING),
            global = true)

    @Override
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            interaction.respondLater();
            Server server = getServer(interaction);
            ServerMusicManager manager = playerManager.getServerMusicManager(server.getId());
            InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();
            Optional<String> valueOpt = interaction.getArgumentStringValueByName("title");
            User user = interaction.getUser();
            String localeCode = interaction.getLocale().getLocaleCode();

            if (valueOpt.isEmpty()) {

                if (!comboCheck(interaction, manager))
                    return;

                if (manager.getPlayer().isPaused()) {
                    manager.getPlayer().setPaused(false);
                    responder.setContent(Message.RESUMED.get(localeCode)).send();
                } else
                    responder.setContent(Message.NOT_PAUSED.get(localeCode)).send();

                return;
            }

            String value = valueOpt.get();

            if (!value.startsWith("http://") && !value.startsWith("https://")) {
                value = "ytsearch:" + value;
            }

            if (getSelf(interaction).getConnectedVoiceChannel(server).isEmpty()) {

                if (user.getConnectedVoiceChannel(server).isEmpty()) {
                    responder.setContent(Message.VC_USER_NOT_CONNECTED.get(localeCode)).send();
                    return;
                }

                user.getConnectedVoiceChannel(server).get().connect().thenAccept(audioConnection -> {

                    audioConnection.setAudioSource(manager.getAudioSource());
                    playerManager.addServerAudioConnection(server.getId(), audioConnection);

                }).exceptionally(e -> {
                    responder.setContent(String.format(Message.ERROR.get(localeCode), e.getMessage())).send();
                    e.printStackTrace();
                    return null;
                });

            } else if (!voiceCheck(interaction))
                return;

            playerManager.loadAndPlay(interaction, value);
        }).start();
    }


}
