package com.eukon05.dilanbot.Listeners;

import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class MusicStopMessageListener extends AbstractMusicMessageListener {

    public MusicStopMessageListener(){
        super(" stop");
    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event) {

        if(me.getConnectedVoiceChannel(event.getServer().get()).isEmpty()){
            channel.sendMessage("I'm not connected to a voice channel!");
            return;
        }

        if(event.getMessageAuthor().getConnectedVoiceChannel().isEmpty() ||
                !(me.getConnectedVoiceChannel(channel.getServer()).get() == event.getMessageAuthor().getConnectedVoiceChannel().get())) {
            channel.sendMessage("You have to be in the same channel as me!");
            return;
        }

        if(manager.player.getPlayingTrack()==null) {
            channel.sendMessage("**:x: Nothing is playing right now**");
            return;
        }

        manager.player.stopTrack();
        manager.scheduler.loopTrack=null;
        manager.scheduler.clearQueue();
        channel.sendMessage("**:no_entry: Music stopped**");

    }

}
