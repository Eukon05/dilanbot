package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.Lavaplayer.PlayerManager;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelMemberLeaveEvent;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class VoiceChannelLeaveListener implements ServerVoiceChannelMemberLeaveListener {

    private final PlayerManager playerManager;

    @Autowired
    public VoiceChannelLeaveListener(PlayerManager playerManager){
        this.playerManager=playerManager;
    }

    @Override
    public void onServerVoiceChannelMemberLeave(ServerVoiceChannelMemberLeaveEvent event) {

        ServerVoiceChannel channel = event.getChannel();
        User me = event.getApi().getYourself();

        if(me.getConnectedVoiceChannel(event.getServer()).isEmpty())
            return;

        if(channel.isConnected(me)){

            int memberCount = channel.getConnectedUsers().size();
            memberCount--;

            if(memberCount==0){
                ServerMusicManager manager = playerManager.getServerMusicManager(event.getServer().getId());
                manager.scheduler.loopTrack=null;
                manager.scheduler.clearQueue();
                manager.player.stopTrack();
                playerManager.getServerAudioConnection(event.getServer().getId()).close();
                playerManager.removeServerAudioConnection(event.getServer().getId());
            }

        }

    }

}
