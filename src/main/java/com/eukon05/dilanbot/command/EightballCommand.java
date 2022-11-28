package com.eukon05.dilanbot.command;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import me.koply.kcommando.internal.OptionType;
import me.koply.kcommando.internal.annotations.HandleSlash;
import me.koply.kcommando.internal.annotations.Option;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
public class EightballCommand {

    private final Gson gson;


    @HandleSlash(name = "8ball", desc = "Ask the 8ball a question!", options = @Option(name = "question", required = true, type = OptionType.STRING), global = true)
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            interaction.respondLater();
            InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();
            String question = interaction.getArgumentStringValueByName("question").get();

            try {
                HttpResponse<String> response = Unirest
                        .get("https://8ball.delegator.com/magic/JSON/" + URLEncoder.encode(question, StandardCharsets.UTF_8))
                        .asString();
                JsonObject responseJson = gson.fromJson(response.getBody(), JsonObject.class).get("magic").getAsJsonObject();

                responder.addEmbed(new EmbedBuilder()
                                .setTitle("The Magic 8-Ball says:")
                                .setDescription(responseJson.get("answer").getAsString())
                                .setFooter("Powered by Delegator"))
                        .send();

            } catch (JsonSyntaxException ex) {
                responder.setContent("Something went wrong: Question contains invalid characters").send();
                ex.printStackTrace();
            } catch (Exception ex) {
                responder.setContent("Something went wrong: " + ex.getMessage()).send();
                ex.printStackTrace();
            }
        }).start();
    }

}
