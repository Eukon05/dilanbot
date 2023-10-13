package com.eukon05.dilanbot.music.lavaplayer;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.Optional;

public record ItemLoadResult(Optional<String> playlistName, AudioTrack firstTrack) {
}
