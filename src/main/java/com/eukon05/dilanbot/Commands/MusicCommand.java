package com.eukon05.dilanbot.Commands;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Lavaplayer.PlayerManager;
import com.eukon05.dilanbot.Lavaplayer.ServerMusicManager;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
public abstract class MusicCommand extends Command{

    protected PlayerManager playerManager;

    public MusicCommand(CommandMap commandMap) {
        super(commandMap);
    }

    @Autowired
    public void setPlayerManager(PlayerManager playerManager){
        this.playerManager=playerManager;
    }
    @Override
    public void run(MessageCreateEvent event, ServerDTO serverDTO, String[] arguments){

        User me = event.getApi().getYourself();
        ServerMusicManager manager = playerManager.getServerMusicManager(event.getServer().get().getId());
        run(event, serverDTO, arguments, me, manager);

    }

    public abstract void run(MessageCreateEvent event, ServerDTO serverDTO, String[] arguments, User me, ServerMusicManager manager);

    protected boolean isBotOnVCCheck(User me, MessageCreateEvent event){

        if(me.getConnectedVoiceChannel(event.getServer().get()).isEmpty()){
            event.getChannel().sendMessage("**:x: I'm not connected to a voice channel!**");
            return false;
        }
        else
            return true;

    }

    protected boolean isUserOnVCCheck(User me, MessageCreateEvent event){

        if(event.getMessageAuthor().getConnectedVoiceChannel().isEmpty() ||
                !(me.getConnectedVoiceChannel(event.getServerTextChannel().get().getServer()).get() == event.getMessageAuthor().getConnectedVoiceChannel().get())) {
            event.getChannel().sendMessage("**:x: You have to be in the same voice channel as me!**");
            return false;
        }
        else
            return true;

    }

    protected boolean isMusicPlayingCheck(ServerMusicManager manager, MessageCreateEvent event){

        if(manager.player.getPlayingTrack()==null) {
            event.getChannel().sendMessage("**:x: Nothing is playing right now**");
            return false;
        }
        else
            return true;

    }

    protected boolean comboCheck(User me, MessageCreateEvent event, ServerMusicManager manager){
        return isBotOnVCCheck(me, event) && isUserOnVCCheck(me, event) && isMusicPlayingCheck(manager, event);
    }


}
