package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class MusicStopCommand extends MusicCommand {

    public MusicStopCommand(CommandMap commandMap) {
        super(commandMap);
        addToCommands("stop");
    }

    @Override
    public void run(MessageCreateEvent event, DiscordServer discordServer, String[] arguments, User me, ServerMusicManager manager) {
        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            if (!comboCheck(me, event, manager))
                return;

            manager.getPlayer().stopTrack();
            manager.getScheduler().setLoopTrack(null);
            manager.getPlayer().setPaused(false);
            manager.getScheduler().clearQueue();
            channel.sendMessage("**:no_entry: Music stopped**");

        }).start();
    }
}
