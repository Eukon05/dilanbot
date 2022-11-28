package com.eukon05.dilanbot.listener;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.listener.server.ServerJoinListener;

public class ServerJoinListenerImpl implements ServerJoinListener {

    @Override
    public void onServerJoin(ServerJoinEvent event) {

        try {
            event.getServer().getSystemChannel().ifPresent(channel -> {

                MessageBuilder msg = new MessageBuilder();
                EmbedBuilder emb = new EmbedBuilder();

                emb.setTitle("Thanks for adding me!");
                emb.setDescription("If you like this bot, please give us a star on [GitHub](https://github.com/Eukon05/dilanbot)");
                emb.setThumbnail(event.getApi().getYourself().getAvatar());

                msg.setEmbed(emb);
                msg.send(channel);

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
