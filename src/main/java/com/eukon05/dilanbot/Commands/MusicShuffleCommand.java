package com.eukon05.dilanbot.Commands;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class MusicShuffleCommand extends MusicCommand{

    @Autowired
    public MusicShuffleCommand(CommandMap commandMap) {
        super(commandMap);
        addToCommands("shuffle");
    }

    @Override
    public void run(MessageCreateEvent event, ServerDTO serverDTO, String[] arguments, User me, ServerMusicManager manager) {
        new Thread(() -> {
            ServerTextChannel channel = event.getServerTextChannel().get();

            if (!isBotOnVCCheck(me, event) || !isUserOnVCCheck(me, event))
                return;

            int queueSize = manager.scheduler.getQueue().size();

            if (queueSize == 0) {
                channel.sendMessage("**The queue is empty!**");
                return;
            }

            Collections.shuffle(manager.scheduler.getQueue());

            channel.sendMessage("**:twisted_rightwards_arrows: Shuffled the queue**");
        }).start();
    }

}
