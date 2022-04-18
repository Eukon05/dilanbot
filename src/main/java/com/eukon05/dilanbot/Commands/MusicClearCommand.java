package com.eukon05.dilanbot.Commands;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicClearCommand extends MusicCommand {

    @Autowired
    public MusicClearCommand(CommandMap commandMap){
        super(commandMap);
        addToCommands("clear");
    }

    @Override
    public void run(MessageCreateEvent event, ServerDTO serverDTO, String[] arguments, User me, ServerMusicManager manager) {

        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            if(!isBotOnVCCheck(me, event) || !isUserOnVCCheck(me, event))
                return;

            int queueSize = manager.scheduler.getQueue().size();

            if(queueSize==0){
                channel.sendMessage("**The queue is empty!**");
                return;
            }

            manager.scheduler.clearQueue();

            if(queueSize==1)
                channel.sendMessage("**Removed 1 track from the queue!**");
            else
                channel.sendMessage("**Removed " + queueSize + " tracks from the queue!**");
        }).start();


    }
}
