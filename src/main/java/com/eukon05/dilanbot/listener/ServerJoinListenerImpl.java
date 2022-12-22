package com.eukon05.dilanbot.listener;

import com.eukon05.dilanbot.MessageUtils;
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
                String localeCode = event.getServer().getPreferredLocale().toLanguageTag();

                emb.setTitle(MessageUtils.getMessage("JOIN_TITLE", localeCode));
                emb.setDescription(MessageUtils.getMessage("JOIN_DESC", localeCode));
                emb.setThumbnail(event.getApi().getYourself().getAvatar());

                msg.setEmbed(emb);
                msg.send(channel);

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
