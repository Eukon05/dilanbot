package com.gotardpl.dilanbot.Listeners;

import core.GLA;
import genius.SongSearch;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MusicLyricsMessageListener extends AbstractMusicMessageListener{

    private final GLA gla;


    @Autowired
    public MusicLyricsMessageListener(GLA gla) {
        super(" lyrics");
        this.gla=gla;
    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event) {

        SongSearch search;
        SongSearch.Hit hit=null;

        if(value.isEmpty()){

            if(me.getConnectedVoiceChannel(event.getServer().get()).isEmpty()){
                channel.sendMessage("I'm not connected to a voice channel!");
                return;
            }

            if(!(me.getConnectedVoiceChannel(channel.getServer()).get() == event.getMessageAuthor().getConnectedVoiceChannel().get())){
                channel.sendMessage("You have to be in the same channel as me!");
                return;
            }

            if(manager.player.getPlayingTrack()==null) {
                channel.sendMessage("**:x: Nothing is playing right now**");
                return;
            }


            try{

                search = gla.search(manager.player.getPlayingTrack().getInfo().title);
                hit = search.getHits().getFirst();

            }

            catch (Exception e) {

                if(e.getMessage()==null)
                    channel.sendMessage("Couldn't find the song on Genius, sorry :(");

                else
                    channel.sendMessage("Something went wrong: " + e.getMessage());

                e.printStackTrace();

            }

        }
        else{

            try{

                search = gla.search(value);
                hit = search.getHits().getFirst();

            }

            catch (Exception e) {

                if(e.getMessage()==null)
                    channel.sendMessage("Couldn't find the song on Genius, sorry :(");

                else
                    channel.sendMessage("Something went wrong: " + e.getMessage());

                e.printStackTrace();

            }

        }

        MessageBuilder builder = new MessageBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setAuthor(hit.getArtist().getName());
        embedBuilder.setTitle(hit.getTitle());
        embedBuilder.setDescription(hit.fetchLyrics());
        embedBuilder.setImage(hit.getImageUrl());
        embedBuilder.setFooter("*Powered by Genius.com*");

        builder.setEmbed(embedBuilder);

        builder.send(channel);



    }
}
