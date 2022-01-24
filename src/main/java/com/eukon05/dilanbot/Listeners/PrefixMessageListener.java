package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

@Component
public class PrefixMessageListener extends AbstractMessageListener {

    public PrefixMessageListener(){
        super(" prefix");
        setAllowDefaultPrefix(true);
    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event, ServerDTO serverDTO, String value) {

        Thread thread = new Thread(){

            @Override
            public void run(){

                ServerTextChannel channel = event.getServerTextChannel().get();

                if(value.isEmpty()){
                    channel.sendMessage("My current prefix is \"" +serverDTO.getPrefix()+"\", to change it, type: \n" +
                            serverDTO.getPrefix()+" prefix [new prefix here]");
                    return;
                }

                serverDTO.setPrefix(value);
                serverService.updateServer(serverDTO);

                channel.sendMessage("Done, my prefix will be \"" + value + "\" from now on!");

            }

        };

        thread.start();



    }

}
