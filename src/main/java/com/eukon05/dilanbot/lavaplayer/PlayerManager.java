package com.eukon05.dilanbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

import java.util.HashMap;

public class PlayerManager {

    //Code copied from/inspired by MenuDocs, go check him out on github!
    //https://github.com/MenuDocs/JDA4-tutorials/blob/EP28/src/main/java/me/duncte123/jdatuts/lavaplayer/PlayerManager.java

    private final AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    private final DiscordApi api;

    public PlayerManager(DiscordApi api) {
        this.api = api;
        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());
    }

    private final HashMap<Long, ServerMusicManager> playersMap = new HashMap<>();
    private final HashMap<Long, AudioConnection> connectionsMap = new HashMap<>();

    public ServerMusicManager getServerMusicManager(Long serverID) {
        playersMap.putIfAbsent(serverID, new ServerMusicManager(audioPlayerManager, api));
        return playersMap.get(serverID);
    }

    public AudioConnection getServerAudioConnection(Long serverID) {
        return connectionsMap.get(serverID);
    }

    public void removeServerAudioConnection(Long serverID) {
        connectionsMap.remove(serverID);
    }

    public void addServerAudioConnection(Long serverID, AudioConnection audioConnection) {
        connectionsMap.put(serverID, audioConnection);
    }

    public void loadAndPlay(SlashCommandInteraction interaction, String trackUrl) {
        ServerMusicManager manager = getServerMusicManager(interaction.getServer().get().getId());
        InteractionFollowupMessageBuilder builder = interaction.createFollowupMessageBuilder();

        audioPlayerManager.loadItemOrdered(manager, trackUrl, new AudioLoadResultHandler() {
            String action = "Playing";

            @Override
            public void trackLoaded(AudioTrack track) {
                if (manager.getPlayer().getPlayingTrack() != null)
                    action = "Queued";

                manager.getScheduler().queue(track);

                builder.addEmbed(new EmbedBuilder()
                                .setTitle(action)
                                .setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")")
                                .setThumbnail(track.getInfo().artworkUrl))
                        .send();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (manager.getPlayer().getPlayingTrack() != null)
                    action = "Queued";

                if (!playlist.isSearchResult()) {

                    for (AudioTrack track : playlist.getTracks()) {
                        manager.getScheduler().queue(track);
                    }

                    builder.addEmbed(new EmbedBuilder()
                                    .setDescription("*:notes: " + action + " playlist \"" + playlist.getName() + "\"*"))
                            .send();
                } else {

                    manager.getScheduler().queue(playlist.getTracks().get(0));

                    builder.addEmbed(new EmbedBuilder()
                                    .setTitle(action)
                                    .setDescription("[" + playlist.getTracks().get(0).getInfo().title + "](" + playlist.getTracks().get(0).getInfo().uri + ")")
                                    .setThumbnail(playlist.getTracks().get(0).getInfo().artworkUrl))
                            .send();
                }
            }

            @Override
            public void noMatches() {
                builder.setContent("No matching tracks found :c").send();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                builder.setContent("Something went wrong! " + e.getMessage()).send();
                e.printStackTrace();
            }
        });
    }

}
