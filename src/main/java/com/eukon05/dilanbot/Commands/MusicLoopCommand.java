package com.eukon05.dilanbot.Commands;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicLoopCommand extends MusicCommand {

    @Autowired
    public MusicLoopCommand(CommandMap commandMap) {
        super(commandMap);
        addToCommands("loop");
    }

    @Override
    public void run(MessageCreateEvent event, ServerDTO serverDTO, String[] arguments, User me, ServerMusicManager manager) {

        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            if(!comboCheck(me, event, manager))
                return;

            if(manager.scheduler.loopTrack==null) {
                manager.scheduler.loopTrack = manager.player.getPlayingTrack();
                channel.sendMessage("**:repeat_one: Looping " + manager.scheduler.loopTrack.getInfo().title + "**");
            }
            else {
                manager.scheduler.loopTrack=null;
                channel.sendMessage("**:warning: Loop disabled!**");
            }

        }).start();

    }
}
