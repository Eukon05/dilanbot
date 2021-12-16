package com.gotardpl.dilanbot.Listeners;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gotardpl.dilanbot.DTOs.ServerDTO;
import com.gotardpl.dilanbot.Services.ServerService;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EightballMessageListener implements MessageCreateListener {

    private final ServerService serverService;
    private final String keyWord = " 8ball";
    private final Gson gson;

    @Autowired
    public EightballMessageListener(ServerService serverService, Gson gson){
        this.serverService=serverService;
        this.gson = gson;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        ServerDTO serverDTO = serverService.getServerById(event.getServer().get().getId());
        String message = event.getMessageContent();
        ServerTextChannel channel = event.getServerTextChannel().get();

        if(!message.startsWith(serverDTO.getPrefix() + keyWord))
            return;

        String value = message.replaceFirst(serverDTO.getPrefix() + keyWord, "").trim();

        try {

            HttpResponse<String> response = Unirest.get("https://8ball.delegator.com/magic/JSON/" + value).asString();
            JsonObject responseJson = gson.fromJson(response.getBody(), JsonObject.class).get("magic").getAsJsonObject();

            channel.sendMessage(responseJson.get("answer").getAsString());

        }
        catch (Exception ex){
            channel.sendMessage("Something went wrong! " + ex.getMessage());
            ex.printStackTrace();
        }

    }

}
