package com.eukon05.dilanbot.Commands;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicStopCommand extends MusicCommand {

    @Autowired
    public MusicStopCommand(CommandMap commandMap){
        super(commandMap);
        addToCommands("stop");
    }

    @Override
    public void run(MessageCreateEvent event, ServerDTO serverDTO, String[] arguments, User me, ServerMusicManager manager) {
        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            if(!comboCheck(me, event, manager))
                return;

            manager.player.stopTrack();
            manager.scheduler.loopTrack=null;
            manager.scheduler.clearQueue();
            channel.sendMessage("**:no_entry: Music stopped**");

        }).start();
    }
}
