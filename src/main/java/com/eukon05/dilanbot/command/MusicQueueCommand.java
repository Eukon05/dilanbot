package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class MusicQueueCommand extends MusicCommand {

    public MusicQueueCommand(CommandMap commandMap) {
        super(commandMap);
        addToCommands("queue");
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

            int pages = queueSize / 5;

            if (pages == 0)
                pages = 1;

            else if (queueSize % 5 != 0)
                pages = pages + 1;


            int page;
            if (arguments.length == 1)
                page = 1;
            else if (Integer.parseInt(arguments[1]) > pages) {
                channel.sendMessage("**:x: There aren't that many pages**");
                return;
            } else
                page = Integer.parseInt(arguments[1]);

            MessageBuilder messageBuilder = new MessageBuilder();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Tracks in the queue");
            embedBuilder.setFooter("Page " + page + "/" + pages);

            StringBuilder content = new StringBuilder();

            for (int i = 5 * (page - 1); i < 5 * page; i++) {
                if (i >= queueSize)
                    break;

                content.append(i + 1)
                        .append(".")
                        .append(" ")
                        .append("[")
                        .append(manager.getScheduler().getQueue().get(i).getInfo().title)
                        .append("](")
                        .append(manager.getScheduler().getQueue().get(i).getInfo().uri)
                        .append(")")
                        .append("\n");
            }

            embedBuilder.setDescription(content.toString());
            messageBuilder.setEmbed(embedBuilder);

            messageBuilder.send(channel);
        }).start();
    }
}
