package com.eukon05.dilanbot.listener;

import com.eukon05.dilanbot.music.MusicService;
import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelMemberLeaveEvent;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;


@RequiredArgsConstructor
public class VoiceChannelLeaveListener implements ServerVoiceChannelMemberLeaveListener {
    private final MusicService service;

    @Override
    public void onServerVoiceChannelMemberLeave(ServerVoiceChannelMemberLeaveEvent event) {
        ServerVoiceChannel channel = event.getChannel();
        User me = event.getApi().getYourself();

        if (channel.isConnected(me)) {
            int memberCount = channel.getConnectedUsers().size();
            memberCount--;

            if (memberCount == 0) {
                service.disconnect(channel);
            }
        }
    }

}
