package com.eukon05.dilanbot.Lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class PlayerManager {

    //Code copied from/inspired by MenuDocs, go check him out on github!
    //https://github.com/MenuDocs/JDA4-tutorials/blob/EP28/src/main/java/me/duncte123/jdatuts/lavaplayer/PlayerManager.java

    private final AudioPlayerManager playerManager;
    public DiscordApi api = null;

    @Autowired
    public PlayerManager(AudioPlayerManager playerManager){
        this.playerManager=playerManager;
    }

    private final HashMap<Long, ServerMusicManager> playersMap = new HashMap<>();
    private final HashMap<Long, AudioConnection> connectionsMap = new HashMap<>();

    public ServerMusicManager getServerMusicManager(Long serverID){

        ServerMusicManager manager = playersMap.get(serverID);

        if(manager==null){
            manager = new ServerMusicManager(playerManager, api);
            playersMap.put(serverID, manager);
        }

        return manager;

    }

    public AudioConnection getServerAudioConnection(Long serverID){

        return connectionsMap.get(serverID);

    }

    public void removeServerAudioConnection(Long serverID){
        connectionsMap.remove(serverID);
    }

    public void addServerAudioConnection(Long serverID, AudioConnection audioConnection){
        connectionsMap.put(serverID, audioConnection);
    }

    public void loadAndPlay(ServerTextChannel textChannel, String trackUrl){

        ServerMusicManager manager = getServerMusicManager(textChannel.getServer().getId());

        playerManager.loadItemOrdered(manager, trackUrl, new AudioLoadResultHandler() {

            String action = "Playing";

            @Override
            public void trackLoaded(AudioTrack track) {

                if(manager.player.getPlayingTrack()!=null)
                    action = "Queued";

                manager.scheduler.queue(track);

                new MessageBuilder().setEmbed(new EmbedBuilder()
                        .setDescription("*:notes: "+action+" ["+track.getInfo().title+"]("+track.getInfo().uri+")*"))
                        .send(textChannel);


            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

                if(manager.player.getPlayingTrack()!=null)
                    action = "Queued";

                if(!playlist.isSearchResult()) {

                    for (AudioTrack track : playlist.getTracks()) {
                        manager.scheduler.queue(track);
                    }

                    new MessageBuilder().setEmbed(new EmbedBuilder()
                                    .setDescription("*:notes: " + action + " playlist \"" + playlist.getName() + "\"*"))
                            .send(textChannel);
                }

                else {

                    manager.scheduler.queue(playlist.getTracks().get(0));

                    new MessageBuilder()
                            .setEmbed(new EmbedBuilder().setDescription("*:notes: "+action+" ["+playlist.getTracks().get(0).getInfo().title+"]("+playlist.getTracks().get(0).getInfo().uri+")*"))
                            .send(textChannel);

                }

            }

            @Override
            public void noMatches() {
                textChannel.sendMessage("No matching tracks found O-O");
            }

            @Override
            public void loadFailed(FriendlyException e) {
                textChannel.sendMessage("Something went wrong! " + e.getMessage());
                e.printStackTrace();
            }
        });

    }

}
