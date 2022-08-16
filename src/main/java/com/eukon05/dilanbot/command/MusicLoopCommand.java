package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class MusicLoopCommand extends MusicCommand {

    public MusicLoopCommand(CommandMap commandMap) {
        super(commandMap);
        addToCommands("loop");
    }

    @Override
    public void run(MessageCreateEvent event, DiscordServer discordServer, String[] arguments, User me, ServerMusicManager manager) {

        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            if (!comboCheck(me, event, manager))
                return;

            if (manager.getScheduler().getLoopTrack() == null) {
                manager.getScheduler().setLoopTrack(manager.getPlayer().getPlayingTrack());
                channel.sendMessage("**:repeat_one: Looping " + manager.getScheduler().getLoopTrack().getInfo().title + "**");
            } else {
                manager.getScheduler().setLoopTrack(null);
                channel.sendMessage("**:warning: Loop disabled!**");
            }

        }).start();

    }
}
