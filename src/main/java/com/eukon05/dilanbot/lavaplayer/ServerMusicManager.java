package com.eukon05.dilanbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import lombok.Getter;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioSource;

//Code copied from/inspired by MenuDocs and Lavaplayer samples
//MenuDocs's github: https://github.com/MenuDocs/JDA4-tutorials/blob/EP28/src/main/java/me/duncte123/jdatuts/lavaplayer/PlayerManager.java

@Getter
public class ServerMusicManager {

    private final AudioPlayer player;
    private final TrackScheduler scheduler;
    private final AudioSource audioSource;

    public ServerMusicManager(AudioPlayerManager playerManager, DiscordApi api) {
        player = playerManager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
        audioSource = new LavaplayerAudioSource(api, player);
    }


}
