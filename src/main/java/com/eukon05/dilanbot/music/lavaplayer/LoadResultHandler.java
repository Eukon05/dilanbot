package com.eukon05.dilanbot.music.lavaplayer;

import com.eukon05.dilanbot.music.exception.NoMatchesException;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.Getter;

import java.util.Optional;

public class LoadResultHandler implements AudioLoadResultHandler {
    private final TrackScheduler scheduler;

    @Getter
    private ItemLoadResult lastLoadResult;

    LoadResultHandler(TrackScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        scheduler.queue(audioTrack);
        lastLoadResult = new ItemLoadResult(Optional.empty(), audioTrack);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        AudioTrack firstTrack = playlist.getTracks().get(0);
        if (!playlist.isSearchResult()) {
            for (AudioTrack track : playlist.getTracks()) {
                scheduler.queue(track);
            }
            lastLoadResult = new ItemLoadResult(Optional.of(playlist.getName()), firstTrack);
        } else {
            scheduler.queue(firstTrack);
            lastLoadResult = new ItemLoadResult(Optional.empty(), firstTrack);
        }
    }

    @Override
    public void noMatches() {
        throw new NoMatchesException();
    }

    @Override
    public void loadFailed(FriendlyException e) {
        throw e;
    }
}
