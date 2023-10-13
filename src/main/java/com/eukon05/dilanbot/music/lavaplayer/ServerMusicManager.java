package com.eukon05.dilanbot.music.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import lombok.Getter;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;

import java.util.Optional;

//Code copied from/inspired by MenuDocs and Lavaplayer samples
//MenuDocs's github: https://github.com/MenuDocs/JDA4-tutorials/blob/EP28/src/main/java/me/duncte123/jdatuts/lavaplayer/PlayerManager.java

@Getter
public class ServerMusicManager {

    private final Server server;
    private final AudioPlayer player;
    private final TrackScheduler scheduler;
    private final AudioSource audioSource;
    private Optional<AudioConnection> connectionOptional = Optional.empty();
    private final LoadResultHandler handler;

    public ServerMusicManager(Server server, AudioPlayerManager playerManager) {
        this.server = server;
        player = playerManager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
        audioSource = new LavaplayerAudioSource(server.getApi(), player);
        handler = new LoadResultHandler(scheduler);
    }

    public void connectToVC(ServerVoiceChannel vc) {
        vc.connect().thenAccept(audioConnection -> {
            audioConnection.setAudioSource(audioSource);
            connectionOptional = Optional.of(audioConnection);
        });
    }

    public void disconnectFromVC() {
        connectionOptional.ifPresent(AudioConnection::close);
        connectionOptional = Optional.empty();
    }

}
