package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.MessageUtils;
import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import core.GLA;
import genius.SongSearch;
import me.koply.kcommando.internal.OptionType;
import me.koply.kcommando.internal.annotations.HandleSlash;
import me.koply.kcommando.internal.annotations.Option;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;

public class MusicLyricsCommand extends AbstractMusicCommand {

    private final GLA gla;
    private JsonArray wordList;
    private final Gson gson;
    private final URL wordListUrl;

    public MusicLyricsCommand(GLA gla, Gson gson, String wordListUrl, PlayerManager manager) throws MalformedURLException {
        super(manager);
        this.gla = gla;
        this.gson = gson;
        this.wordListUrl = new URL(wordListUrl);
    }

    @HandleSlash(name = "lyrics",
            desc = "Shows lyrics for specified or currently playing track",
            options = @Option(name = "title", desc = "Title of the song to play", type = OptionType.STRING),
            global = true
    )
    @Override
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            interaction.respondLater();
            ServerMusicManager manager = playerManager.getServerMusicManager(getServer(interaction).getId());
            InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();
            Optional<String> value = interaction.getArgumentStringValueByName("title");
            String localeCode = interaction.getLocale().getLocaleCode();

            try {
                wordList = gson.fromJson(new InputStreamReader(wordListUrl.openStream()), JsonArray.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                responder.setContent(MessageUtils.getMessage("WORDLIST_NOT_FOUND", localeCode)).send();

            } catch (IOException e) {
                e.printStackTrace();
                responder.setContent(String.format(MessageUtils.getMessage("IO_ERROR", localeCode), e.getMessage())).send();
            }

            SongSearch search;
            SongSearch.Hit hit = null;

            if (value.isEmpty()) {

                if (!comboCheck(interaction, manager))
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
                        responder.setContent(MessageUtils.getMessage("GENIUS_NOT_FOUND", localeCode)).send();

                    else
                        responder.setContent(String.format(MessageUtils.getMessage("ERROR", localeCode), e.getMessage())).send();

                    e.printStackTrace();

                }

            } else {

                try {

                    search = gla.search(value.get());
                    hit = search.getHits().getFirst();

                } catch (Exception e) {

                    if (e.getMessage() == null)
                        responder.setContent(MessageUtils.getMessage("GENIUS_NOT_FOUND", localeCode)).send();

                    else
                        responder.setContent(String.format(MessageUtils.getMessage("ERROR", localeCode), e.getMessage())).send();

                    e.printStackTrace();

                }

            }

            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setAuthor(hit.getArtist().getName());
            embedBuilder.setTitle(hit.getTitle());
            embedBuilder.setDescription(hit.fetchLyrics());
            embedBuilder.setImage(hit.getImageUrl());
            embedBuilder.setFooter(MessageUtils.getMessage("GENIUS_FOOTER", localeCode));

            responder.addEmbed(embedBuilder).send();
        }).start();
    }

}
