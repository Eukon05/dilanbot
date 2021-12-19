package com.gotardpl.dilanbot.Listeners;

import com.gotardpl.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicSkipMessageListener extends AbstractMusicMessageListener {

    public MusicSkipMessageListener(){
        super(" skip");
    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event) {

        if(!(me.getConnectedVoiceChannel(channel.getServer()).get() == event.getMessageAuthor().getConnectedVoiceChannel().get())){
            channel.sendMessage("You have to be in the same channel as me!");
            return;
        }

        if(manager.player.getPlayingTrack()==null) {
            channel.sendMessage("**:x: Nothing is playing right now**");
            return;
        }

        if(manager.scheduler.loopTrack!=null){
            manager.scheduler.loopTrack=null;
            channel.sendMessage("**:warning: Loop disabled!**");
        }

        manager.scheduler.nextTrack();
        channel.sendMessage("**:fast_forward: Skipped**");

    }

}
