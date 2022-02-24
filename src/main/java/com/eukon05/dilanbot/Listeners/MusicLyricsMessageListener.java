package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import core.GLA;
import genius.SongSearch;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

@Component
public class MusicLyricsMessageListener extends AbstractMusicMessageListener{

    private final GLA gla;
    private JsonArray wordList;
    private final Gson gson;
    private final URL wordlistUrl;

    @Autowired
    public MusicLyricsMessageListener(GLA gla, Gson gson, @Value("${lyrics.wordlist.path}") String url) throws MalformedURLException {
        super(" lyrics");
        this.gla=gla;
        this.gson = gson;
        this.wordlistUrl = new URL(url);
    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event, ServerDTO serverDTO, String value, User me, ServerMusicManager manager) {

        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            try {
                wordList = gson.fromJson(new InputStreamReader(wordlistUrl.openStream()), JsonArray.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                channel.sendMessage("An I/O error has occurred: " + e.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }

            SongSearch search;
            SongSearch.Hit hit=null;

            if(value.isEmpty()){

                if(me.getConnectedVoiceChannel(event.getServer().get()).isEmpty()){
                    channel.sendMessage("I'm not connected to a voice channel!");
                    return;
                }

                if(event.getMessageAuthor().getConnectedVoiceChannel().isEmpty() ||
                        !(me.getConnectedVoiceChannel(channel.getServer()).get() == event.getMessageAuthor().getConnectedVoiceChannel().get())) {
                    channel.sendMessage("You have to be in the same channel as me!");
                    return;
                }

                if(manager.player.getPlayingTrack()==null) {
                    channel.sendMessage("**:x: Nothing is playing right now**");
                    return;
                }


                try{

                    String title = manager.player.getPlayingTrack().getInfo().title.toLowerCase(Locale.ROOT);

                    for(JsonElement element : wordList){
                        title = title.replace(element.getAsJsonObject().get("value").getAsString(), " ");
                    }

                    search = gla.search(title);
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

        }).start();

    }
}
