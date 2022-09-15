package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import com.eukon05.dilanbot.repository.CommandRepository;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class MusicShuffleCommand extends MusicCommand {

    public MusicShuffleCommand(CommandRepository commandRepository) {
        super("shuffle", commandRepository);
    }

    @Override
    public void run(MessageCreateEvent event, DiscordServer discordServer, String[] arguments, User me, ServerMusicManager manager) {
        new Thread(() -> {
            ServerTextChannel channel = event.getServerTextChannel().get();

            if (!isBotOnVCCheck(me, event) || !isUserOnVCCheck(me, event))
                return;

            int queueSize = manager.getScheduler().getQueue().size();

            if (queueSize == 0) {
                channel.sendMessage("**The queue is empty!**");
                return;
            }

            Collections.shuffle(manager.getScheduler().getQueue());

            channel.sendMessage("**:twisted_rightwards_arrows: Shuffled the queue**");
        }).start();
    }

}
