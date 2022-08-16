package com.eukon05.dilanbot.listener;

import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelMemberLeaveEvent;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class VoiceChannelLeaveListener implements ServerVoiceChannelMemberLeaveListener {

    private final PlayerManager playerManager;

    @Override
    public void onServerVoiceChannelMemberLeave(ServerVoiceChannelMemberLeaveEvent event) {
        ServerVoiceChannel channel = event.getChannel();
        User me = event.getApi().getYourself();

        if (me.getConnectedVoiceChannel(event.getServer()).isEmpty())
            return;

        if (channel.isConnected(me)) {
            int memberCount = channel.getConnectedUsers().size();
            memberCount--;

            if (memberCount == 0) {
                ServerMusicManager manager = playerManager.getServerMusicManager(event.getServer().getId());
                manager.getPlayer().stopTrack();
                manager.getScheduler().setLoopTrack(null);
                manager.getPlayer().setPaused(false);
                manager.getScheduler().clearQueue();
                playerManager.getServerAudioConnection(event.getServer().getId()).close();
                playerManager.removeServerAudioConnection(event.getServer().getId());
            }
        }
    }

}
