package com.eukon05.dilanbot.music.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Optional;

//Code taken from Lavaplayer's samples, thanks!
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;

    @Getter
    private final ArrayList<AudioTrack> queue;

    @Getter
    private Optional<AudioTrack> loopTrack = Optional.empty();

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new ArrayList<>();
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.add(track);
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        if (queue.isEmpty()) {
            player.startTrack(null, false);
            return;
        }

        player.startTrack(queue.get(0), false);
        queue.remove(0);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (loopTrack.isEmpty())
                nextTrack();
            else {
                loopTrack = Optional.of(loopTrack.get().makeClone());
                player.startTrack(loopTrack.get(), true);
            }
        }
    }

    public void setLoopTrack(AudioTrack track) {
        loopTrack = Optional.of(track);
    }

    public void clearLoopTrack() {
        loopTrack = Optional.empty();
    }
}
