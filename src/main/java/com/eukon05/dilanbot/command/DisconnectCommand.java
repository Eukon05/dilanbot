package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import com.eukon05.dilanbot.repository.CommandRepository;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class DisconnectCommand extends MusicCommand {
    public DisconnectCommand(CommandRepository commandRepository) {
        super("disconnect", commandRepository);
    }

    @Override
    public void run(MessageCreateEvent event, DiscordServer discordServer, String[] arguments, User me, ServerMusicManager manager) {
        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            if (!isBotOnVCCheck(me, event) || !isUserOnVCCheck(me, event))
                return;

            //Here the track is stopped and the queue is cleared, I might change this behaviour in the future based on user feedback
            manager.getPlayer().stopTrack();
            manager.getScheduler().clearQueue();

            playerManager.getServerAudioConnection(discordServer.getId()).close();
            playerManager.removeServerAudioConnection(discordServer.getId());

            channel.sendMessage("**Disconnected from " + me.getConnectedVoiceChannel(event.getServer().get()).get().getName() + " **");

        }).start();
    }
}
