package com.gotardpl.dilanbot.Listeners;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class EightballListener implements MessageCreateListener {

    @Autowired
    private Gson gson;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {

        if(messageCreateEvent.getMessageContent().toLowerCase(Locale.ROOT).contains("dilan 8ball")){

            String value = messageCreateEvent.getMessageContent().replaceFirst("dilan 8ball", "");

            try {

                HttpResponse<String> response = Unirest.get("https://8ball.delegator.com/magic/JSON/" + value).asString();
                JsonObject responseJson = gson.fromJson(response.getBody(), JsonObject.class).get("magic").getAsJsonObject();

                messageCreateEvent.getChannel().sendMessage(responseJson.get("answer").getAsString());

            }
            catch (Exception ex){
                messageCreateEvent.getChannel().sendMessage("Something went wrong! " + ex.getMessage());
                ex.printStackTrace();
            }

        }

    }
}
