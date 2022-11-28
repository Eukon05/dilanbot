package com.eukon05.dilanbot.command;

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

        if (myVc.isEmpty()) {
            responder.setContent("**:x: I'm not connected to a voice channel!**").send();
            return false;
        } else if (vc.isEmpty()) {
            responder.setContent("**:x: You're not connected to a voice channel!**").send();
            return false;
        } else if (!vc.get().equals(myVc.get())) {
            responder.setContent("**:x: You have to be in the same voice channel as me!**").send();
            return false;
        } else {
            return true;
        }
    }

    protected boolean isMusicPlayingCheck(SlashCommandInteraction interaction, ServerMusicManager manager) {
        if (manager.getPlayer().getPlayingTrack() == null) {
            interaction.createFollowupMessageBuilder().setContent("**:x: Nothing is playing right now**").send();
            return false;
        } else
            return true;
    }

    protected boolean comboCheck(SlashCommandInteraction interaction, ServerMusicManager manager) {
        return voiceCheck(interaction) && isMusicPlayingCheck(interaction, manager);
    }

    @Override
    public Server getServer(SlashCommandInteraction interaction) {
        Optional<Server> serverOpt = interaction.getServer();
        if (serverOpt.isEmpty()) {
            interaction.createFollowupMessageBuilder().setContent("Music commands cannot be used in DMs!").send();
            Thread.currentThread().stop();
            return null;
        }
        return serverOpt.get();
    }


}
