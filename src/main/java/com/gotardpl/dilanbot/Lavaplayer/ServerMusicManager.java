package com.gotardpl.dilanbot.Lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioSource;

//Code copied from/inspired by MenuDocs and Lavaplayer samples
//MenuDocs's github: https://github.com/MenuDocs/JDA4-tutorials/blob/EP28/src/main/java/me/duncte123/jdatuts/lavaplayer/PlayerManager.java

public class ServerMusicManager {

    private final DiscordApi api;

    public final AudioPlayerManager playerManager;

    public final AudioPlayer player;
    public final TrackScheduler scheduler;
    public final AudioSource audioSource;

    public ServerMusicManager(AudioPlayerManager playerManager, DiscordApi api){
        this.api = api;
        this.playerManager = playerManager;
        player = playerManager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
        audioSource = new LavaplayerAudioSource(api, player);
    }




}
