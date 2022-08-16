package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class MusicPauseCommand extends MusicCommand {

    public MusicPauseCommand(CommandMap commandMap) {
        super(commandMap);
        addToCommands("pause");
    }

    @Override
    public void run(MessageCreateEvent event, DiscordServer discordServer, String[] arguments, User me, ServerMusicManager manager) {
        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            if (!comboCheck(me, event, manager))
                return;

            if (manager.getPlayer().isPaused()) {
                channel.sendMessage("**:x: The player is already paused!**");
                return;
            }

            manager.getPlayer().setPaused(true);
            channel.sendMessage("**:pause_button: Music paused**");

        }).start();
    }
}
