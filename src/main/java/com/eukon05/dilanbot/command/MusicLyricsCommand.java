package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import com.eukon05.dilanbot.repository.CommandRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

@Component
public class MusicLyricsCommand extends MusicCommand {

    private final GLA gla;
    private JsonArray wordList;
    private final Gson gson;
    private final URL wordlistUrl;

    public MusicLyricsCommand(GLA gla, Gson gson, @Value("${lyrics.wordlist.path}") String url, CommandRepository commandRepository) throws MalformedURLException {
        super("lyrics", commandRepository);
        this.gla = gla;
        this.gson = gson;
        this.wordlistUrl = new URL(url);
    }

    @Override
    public void run(MessageCreateEvent event, DiscordServer discordServer, String[] arguments, User me, ServerMusicManager manager) {
        new Thread(() -> {
            ServerTextChannel channel = event.getServerTextChannel().get();

            String value = fuseArguments(arguments);

            try {
                wordList = gson.fromJson(new InputStreamReader(wordlistUrl.openStream()), JsonArray.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                channel.sendMessage("lyrics-wordlist.json file NOT FOUND!");

            } catch (IOException e) {
                e.printStackTrace();
                channel.sendMessage("An I/O error has occurred: " + e.getMessage());
            }

            SongSearch search;
            SongSearch.Hit hit = null;

            if (value.isEmpty()) {

                if (!comboCheck(me, event, manager))
                    return;

                try {

                    String title = manager.getPlayer().getPlayingTrack().getInfo().title.toLowerCase(Locale.ROOT);

                    for (JsonElement element : wordList) {
                        title = title.replace(element.getAsJsonObject().get("value").getAsString(), " ");
                    }

                    search = gla.search(title);
                    hit = search.getHits().getFirst();

                } catch (Exception e) {

                    if (e.getMessage() == null)
                        channel.sendMessage("Couldn't find the song on Genius, sorry :(");

                    else
                        channel.sendMessage("Something went wrong: " + e.getMessage());

                    e.printStackTrace();

                }

            } else {

                try {

                    search = gla.search(value);
                    hit = search.getHits().getFirst();

                } catch (Exception e) {

                    if (e.getMessage() == null)
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
            embedBuilder.setFooter("**Powered by Genius.com**");

            builder.setEmbed(embedBuilder);

            builder.send(channel);
        }).start();
    }

}
