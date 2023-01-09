package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.MessageUtils;
import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import lombok.AllArgsConstructor;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

import java.util.Optional;

@AllArgsConstructor
public abstract class AbstractMusicCommand implements Command {

    protected PlayerManager playerManager;

    protected boolean voiceCheck(SlashCommandInteraction interaction) {
        Server server = getServer(interaction);
        Optional<ServerVoiceChannel> vc = interaction.getUser().getConnectedVoiceChannel(server);
        Optional<ServerVoiceChannel> myVc = getSelf(interaction).getConnectedVoiceChannel(server);
        InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();

        String localeCode = interaction.getLocale().getLocaleCode();

        if (myVc.isEmpty()) {
            responder.setContent(MessageUtils.getMessage("VC_BOT_NOT_CONNECTED", localeCode)).send();
            return false;
        } else if (vc.isEmpty()) {
            responder.setContent(MessageUtils.getMessage("VC_USER_NOT_CONNECTED", localeCode)).send();
            return false;
        } else if (!vc.get().equals(myVc.get())) {
            responder.setContent(MessageUtils.getMessage("VC_DIFFERENT_CHANNELS", localeCode)).send();
            return false;
        } else {
            return true;
        }
    }

    protected boolean isMusicPlayingCheck(SlashCommandInteraction interaction, ServerMusicManager manager) {
        String localeCode = interaction.getLocale().getLocaleCode();
        if (manager.getPlayer().getPlayingTrack() == null) {
            interaction.createFollowupMessageBuilder().setContent(MessageUtils.getMessage("NOT_PLAYING", localeCode)).send();
            return false;
        } else
            return true;
    }

    protected boolean comboCheck(SlashCommandInteraction interaction, ServerMusicManager manager) {
        return voiceCheck(interaction) && isMusicPlayingCheck(interaction, manager);
    }

    @Override
    public Server getServer(SlashCommandInteraction interaction) {
        String localeCode = interaction.getLocale().getLocaleCode();
        Optional<Server> serverOpt = interaction.getServer();
        if (serverOpt.isEmpty()) {
            interaction.createFollowupMessageBuilder().setContent(MessageUtils.getMessage("DM", localeCode)).send();
            Thread.currentThread().stop();
            return null;
        }
        return serverOpt.get();
    }


}