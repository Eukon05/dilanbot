package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class MusicRemoveCommand extends MusicCommand {

    public MusicRemoveCommand(CommandMap commandMap) {
        super(commandMap);
        addToCommands("remove");
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

            int index;

            if (arguments.length == 1)
                index = queueSize - 1;
            else {
                index = Integer.parseInt(arguments[1]);
                if (index < 1) {
                    channel.sendMessage("**The index of the song to remove must be equal to at least 1**");
                    return;
                }
                if (index > queueSize) {
                    channel.sendMessage("**There are less songs in the queue than the index you've specified**");
                    return;
                }
            }

            String title = manager.getScheduler().getQueue().get(index - 1).getInfo().title;
            manager.getScheduler().getQueue().remove(index - 1);
            channel.sendMessage("**Removed \"" + title + "\" from the queue**");


        }).start();

    }


}
