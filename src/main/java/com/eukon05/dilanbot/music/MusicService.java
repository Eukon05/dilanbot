package com.eukon05.dilanbot.music;

import com.eukon05.dilanbot.music.exception.*;
import com.eukon05.dilanbot.music.lavaplayer.ItemLoadResult;
import com.eukon05.dilanbot.music.lavaplayer.LoadResultHandler;
import com.eukon05.dilanbot.music.lavaplayer.ServerMusicManager;
import com.eukon05.dilanbot.music.lavaplayer.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class MusicService {
    private final Map<Long, ServerMusicManager> managers = new HashMap<>();
    private final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

    public MusicService() {
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    ItemLoadResult play(User user, Server server, String title) throws ExecutionException, InterruptedException {
        ServerVoiceChannel channel = user.getConnectedVoiceChannel(server).orElseThrow(UserNotConnectedException::new);
        return play(channel, title);
    }

    ItemLoadResult play(ServerVoiceChannel channel, String title) throws ExecutionException, InterruptedException {
        ServerMusicManager manager = getServerMusicManager(channel.getServer());

        manager.getConnectionOptional().ifPresentOrElse(conn -> {
            if (!conn.getChannel().equals(channel))
                throw new DifferentVCException();
        }, () -> manager.connectToVC(channel));

        if (!title.startsWith("http://") && !title.startsWith("https://")) title = "ytsearch:" + title;

        LoadResultHandler handler = manager.getHandler();

        playerManager.loadItem(title, handler).get();

        return handler.getLastLoadResult();
    }

    void play(ServerVoiceChannel voiceChannel) {
        ServerMusicManager manager = getServerMusicManager(voiceChannel.getServer());
        checkConnection(manager, voiceChannel);

        AudioPlayer player = manager.getPlayer();
        checkIsPlaying(player);

        if (!player.isPaused())
            throw new PlayerNotPausedException();

        player.setPaused(false);
    }

    Optional<AudioTrack> skip(ServerVoiceChannel channel) {
        ServerMusicManager manager = getServerMusicManager(channel.getServer());
        checkConnection(manager, channel);

        AudioPlayer player = manager.getPlayer();
        checkIsPlaying(player);

        manager.getScheduler().clearLoopTrack();
        manager.getScheduler().nextTrack();
        return Optional.ofNullable(player.getPlayingTrack());
    }

    void pause(ServerVoiceChannel channel) {
        ServerMusicManager manager = getServerMusicManager(channel.getServer());
        checkConnection(manager, channel);

        AudioPlayer player = manager.getPlayer();
        checkIsPlaying(player);

        if (player.isPaused())
            throw new PlayerAlreadyPausedException();

        player.setPaused(true);
    }

    void stop(ServerVoiceChannel channel) {
        ServerMusicManager manager = getServerMusicManager(channel.getServer());
        checkConnection(manager, channel);

        AudioPlayer player = manager.getPlayer();
        checkIsPlaying(player);

        TrackScheduler scheduler = manager.getScheduler();

        player.stopTrack();
        scheduler.clearLoopTrack();
        scheduler.getQueue().clear();
        player.setPaused(false);
    }

    long clear(ServerVoiceChannel channel) {
        ServerMusicManager manager = getServerMusicManager(channel.getServer());
        checkConnection(manager, channel);

        ArrayList<AudioTrack> queue = manager.getScheduler().getQueue();

        if (queue.isEmpty())
            throw new EmptyQueueException();

        long removed = queue.size();

        queue.clear();

        return removed;
    }

    void shuffle(ServerVoiceChannel channel) {
        ServerMusicManager manager = getServerMusicManager(channel.getServer());
        checkConnection(manager, channel);

        ArrayList<AudioTrack> queue = manager.getScheduler().getQueue();

        if (queue.isEmpty())
            throw new EmptyQueueException();

        Collections.shuffle(queue);
    }

    Optional<AudioTrack> changeLoopMode(ServerVoiceChannel channel) {
        ServerMusicManager manager = getServerMusicManager(channel.getServer());
        checkConnection(manager, channel);

        AudioPlayer player = manager.getPlayer();
        checkIsPlaying(player);

        if (manager.getScheduler().getLoopTrack().isEmpty()) {
            return Optional.of(loop(manager));
        } else {
            unloop(manager);
            return Optional.empty();
        }
    }

    AudioTrack nowPlaying(ServerVoiceChannel channel) {
        ServerMusicManager manager = getServerMusicManager(channel.getServer());
        checkConnection(manager, channel);

        AudioPlayer player = manager.getPlayer();
        checkIsPlaying(player);

        return player.getPlayingTrack();
    }

    public void disconnect(ServerVoiceChannel channel) {
        ServerMusicManager manager = getServerMusicManager(channel.getServer());
        checkConnection(manager, channel);

        manager.getPlayer().stopTrack();
        manager.getScheduler().clearLoopTrack();
        manager.getScheduler().getQueue().clear();
        manager.disconnectFromVC();
    }

    private AudioTrack loop(ServerMusicManager manager) {
        AudioTrack track = manager.getPlayer().getPlayingTrack();
        manager.getScheduler().setLoopTrack(track);
        return track;
    }

    private void unloop(ServerMusicManager manager) {
        manager.getScheduler().clearLoopTrack();
    }

    private ServerMusicManager getServerMusicManager(Server server) {
        managers.putIfAbsent(server.getId(), new ServerMusicManager(server, playerManager));
        return managers.get(server.getId());
    }

    private void checkConnection(ServerMusicManager manager, ServerVoiceChannel targetVoiceChannel) {
        AudioConnection connection = manager.getConnectionOptional().orElseThrow(BotNotConnectedException::new);

        if (!connection.getChannel().equals(targetVoiceChannel))
            throw new DifferentVCException();
    }

    private void checkIsPlaying(AudioPlayer player) {
        if (player.getPlayingTrack() == null)
            throw new NothingPlayingException();
    }

}
