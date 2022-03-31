package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
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
    void childOnMessageCreate(MessageCreateEvent event, ServerDTO serverDTO, String value) {

        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            try {

                HttpResponse<String> response = Unirest.get("https://8ball.delegator.com/magic/JSON/" + value).asString();
                JsonObject responseJson = gson.fromJson(response.getBody(), JsonObject.class).get("magic").getAsJsonObject();

                new MessageBuilder().setEmbed(new EmbedBuilder()
                        .setTitle("The Magic 8-Ball says:")
                        .setDescription(responseJson.get("answer").getAsString())
                        .setFooter("Powered by Delegator")
                ).send(channel);

            }
            catch (Exception ex){
                channel.sendMessage("Something went wrong! " + ex.getMessage());
                ex.printStackTrace();
            }

        }).start();

    }

}
