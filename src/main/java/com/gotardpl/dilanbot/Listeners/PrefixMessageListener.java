package com.gotardpl.dilanbot.Listeners;

import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrefixMessageListener extends AbstractMessageListener {

    @Autowired
    public PrefixMessageListener(){
        super(" prefix");
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        super.onMessageCreate(event);

        if(!isCorrectListener)
            return;

        if(value.isEmpty()){
            channel.sendMessage("My current prefix is \"" +serverDTO.getPrefix()+"\", to change it, type: \n" +
                    serverDTO.getPrefix()+" prefix [new prefix here]");
            return;
        }

        serverDTO.setPrefix(value);
        serverService.updateServer(serverDTO);

        channel.sendMessage("Done, my prefix will be \"" + value + "\" from now on!");

    }

}
