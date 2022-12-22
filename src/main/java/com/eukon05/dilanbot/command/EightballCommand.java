package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.MessageUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

    private static final String EIGHTBALL_API_URL = "https://eightballapi.com/api?question=%s";


    @HandleSlash(name = "8ball", desc = "Ask the 8ball a question!", options = @Option(name = "question", required = true, type = OptionType.STRING), global = true)
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            interaction.respondLater();
            InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();
            String question = interaction.getArgumentStringValueByName("question").get();
            String localeCode = interaction.getLocale().getLocaleCode();

            try {
                HttpResponse<String> response = Unirest
                        .get(String.format(EIGHTBALL_API_URL, URLEncoder.encode(question, StandardCharsets.UTF_8)))
                        .asString();
                String apiResponse = gson.fromJson(response.getBody(), JsonObject.class).get("reading").getAsString();

                responder.addEmbed(new EmbedBuilder()
                                .setAuthor(interaction.getUser())
                                .setTitle(question)
                                .setDescription(apiResponse)
                                .setFooter(MessageUtils.getMessage("8BALL_FOOTER", localeCode)))
                        .send();

            } catch (Exception ex) {
                responder.setContent(String.format(MessageUtils.getMessage("ERROR", localeCode), ex.getMessage())).send();
                ex.printStackTrace();
            }
        }).start();
    }

}
