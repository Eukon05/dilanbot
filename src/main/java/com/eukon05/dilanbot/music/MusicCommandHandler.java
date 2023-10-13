package com.eukon05.dilanbot.music;

import com.eukon05.dilanbot.DilanException;
import com.eukon05.dilanbot.Message;
import com.eukon05.dilanbot.music.exception.UserNotConnectedException;
import com.eukon05.dilanbot.music.lavaplayer.ItemLoadResult;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.koply.kcommando.internal.OptionType;
import me.koply.kcommando.internal.annotations.HandleSlash;
import me.koply.kcommando.internal.annotations.Option;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.eukon05.dilanbot.Message.MARKDOWN_URL;

public class MusicCommandHandler {
    private static final String TITLE_ARGUMENT = "title";
    private final MusicService service;

    public MusicCommandHandler(MusicService service) {
        this.service = service;
    }

    @HandleSlash(name = "play",
            desc = "Plays the specified track or disables pause",
            options = @Option(name = TITLE_ARGUMENT, desc = "Name of the song to play", type = OptionType.STRING),
            global = true)
    public void handlePlay(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        interaction.respondLater();
        String locale = interaction.getLocale().getLocaleCode();
        InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();

        Server server = interaction.getServer().orElseThrow();
        Optional<String> title = interaction.getArgumentStringValueByName(TITLE_ARGUMENT);
        User user = interaction.getUser();

        title.ifPresentOrElse(t -> {
            try {
                ItemLoadResult result = service.play(user, server, t);
                EmbedBuilder embed = new EmbedBuilder();
                String action = Message.QUEUED.get(locale);
                AudioTrackInfo firstTrackInfo = result.firstTrack().getInfo();

                embed.setTitle(action);

                result.playlistName().ifPresentOrElse(embed::setDescription,
                        () -> embed.setDescription(String.format(MARKDOWN_URL, firstTrackInfo.title, firstTrackInfo.uri)));

                embed.setThumbnail(firstTrackInfo.artworkUrl);

                responder.addEmbed(embed).send();
            } catch (DilanException e) {
                e.handle(responder, locale);
            } catch (ExecutionException | InterruptedException e) {
                responder.setContent(String.format(Message.ERROR.get(locale), e.getMessage())).send();
            }
        }, () -> {
            try {
                service.play(user.getConnectedVoiceChannel(server).orElseThrow(UserNotConnectedException::new));
                responder.setContent(Message.RESUMED.get(locale)).send();
            } catch (DilanException e) {
                e.handle(responder, locale);
            }
        });
    }

    @HandleSlash(name = "skip", desc = "Skips the currently playing track", global = true)
    public void handleSkip(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        interaction.respondLater();
        String locale = interaction.getLocale().getLocaleCode();
        InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();

        Server server = interaction.getServer().orElseThrow();
        User user = interaction.getUser();
        try {
            Optional<AudioTrack> result = service.skip(user.getConnectedVoiceChannel(server).orElseThrow(UserNotConnectedException::new));
            responder.setContent(Message.SKIPPED.get(locale));

            result.ifPresent(track -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(Message.PLAYING.get(locale))
                        .setDescription(String.format(MARKDOWN_URL, track.getInfo().title, track.getInfo().uri))
                        .setThumbnail(track.getInfo().artworkUrl);

                responder.addEmbed(embed);
            });
            responder.send();
        } catch (DilanException e) {
            e.handle(responder, locale);
        }
    }

    @HandleSlash(name = "pause", desc = "Pauses the track", global = true)
    public void handlePause(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        interaction.respondLater();
        String locale = interaction.getLocale().getLocaleCode();
        InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();

        Server server = interaction.getServer().orElseThrow();
        User user = interaction.getUser();

        try {
            service.pause(user.getConnectedVoiceChannel(server).orElseThrow(UserNotConnectedException::new));
            responder.setContent(Message.PAUSED.get(locale)).send();
        } catch (DilanException e) {
            e.handle(responder, locale);
        }
    }

    @HandleSlash(name = "clear", desc = "Clears the queue", global = true)
    public void handleClear(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        interaction.respondLater();
        String locale = interaction.getLocale().getLocaleCode();
        InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();

        Server server = interaction.getServer().orElseThrow();
        User user = interaction.getUser();

        try {
            long removed = service.clear(user.getConnectedVoiceChannel(server).orElseThrow(UserNotConnectedException::new));
            responder.setContent(String.format(Message.QUEUE_REMOVED_TRACKS.get(locale), removed)).send();
        } catch (DilanException e) {
            e.handle(responder, locale);
        }
    }

    @HandleSlash(name = "stop", desc = "Stops the music and clears the queue", global = true)
    public void handleStop(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        interaction.respondLater();
        String locale = interaction.getLocale().getLocaleCode();
        InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();

        Server server = interaction.getServer().orElseThrow();
        User user = interaction.getUser();

        try {
            service.stop(user.getConnectedVoiceChannel(server).orElseThrow(UserNotConnectedException::new));
            responder.setContent(Message.STOPPED.get(locale)).send();
        } catch (DilanException e) {
            e.handle(responder, locale);
        }
    }

    @HandleSlash(name = "disconnect", desc = "Disconnects the bot from a voice channel (if it's connected to one)", global = true)
    public void handleDisconnect(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        interaction.respondLater();
        String locale = interaction.getLocale().getLocaleCode();
        InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();

        Server server = interaction.getServer().orElseThrow();
        User user = interaction.getUser();

        try {
            ServerVoiceChannel channel = user.getConnectedVoiceChannel(server).orElseThrow(UserNotConnectedException::new);
            service.disconnect(channel);
            responder.setContent(String.format(Message.DISCONNECT.get(locale), channel.getName())).send();
        } catch (DilanException e) {
            e.handle(responder, locale);
        }
    }

    @HandleSlash(name = "np", desc = "Shows the currently playing track", global = true)
    public void handleNowPlaying(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        interaction.respondLater();
        String locale = interaction.getLocale().getLocaleCode();
        InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();

        Server server = interaction.getServer().orElseThrow();
        User user = interaction.getUser();

        try {
            AudioTrack track = service.nowPlaying(user.getConnectedVoiceChannel(server).orElseThrow(UserNotConnectedException::new));

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(Message.NP.get(locale))
                    .setDescription(String.format(MARKDOWN_URL, track.getInfo().title, track.getInfo().uri))
                    .setThumbnail(track.getInfo().artworkUrl);

            responder.addEmbed(embed).send();
        } catch (DilanException e) {
            e.handle(responder, locale);
        }
    }

    @HandleSlash(name = "loop", desc = "Enables or disables looping of the current track", global = true)
    public void handleLoop(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        interaction.respondLater();
        String locale = interaction.getLocale().getLocaleCode();
        InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();

        Server server = interaction.getServer().orElseThrow();
        User user = interaction.getUser();

        try {
            Optional<AudioTrack> trackOptional = service.changeLoopMode(user.getConnectedVoiceChannel(server).orElseThrow(UserNotConnectedException::new));

            trackOptional.ifPresentOrElse(track -> {
                AudioTrackInfo info = track.getInfo();

                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(Message.LOOP_ENABLED.get(locale))
                        .setDescription(String.format(MARKDOWN_URL, info.title, info.uri))
                        .setThumbnail(info.artworkUrl);

                responder.addEmbed(embed);
            }, () -> responder.setContent(Message.LOOP_DISABLED.get(locale)));

            responder.send();
        } catch (DilanException e) {
            e.handle(responder, locale);
        }
    }

}
