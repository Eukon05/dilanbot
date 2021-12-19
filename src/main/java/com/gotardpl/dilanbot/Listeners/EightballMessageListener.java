package com.gotardpl.dilanbot.Listeners;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EightballMessageListener extends AbstractMessageListener {

    private final Gson gson;

    @Autowired
    public EightballMessageListener(Gson gson){
        super(" 8ball");
        this.gson = gson;
    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event) {

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
