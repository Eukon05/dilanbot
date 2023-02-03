package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.Message;
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
public class RedditCommand implements Command {

    private final Gson gson;

    private static final String REDDIT_API_URL = "https://www.reddit.com/r/%s/randomrising.json?limit=1";

    @Override
    @HandleSlash(name = "reddit", desc = "Get a random post from a specified subreddit", options = @Option(name = "subreddit", required = true, type = OptionType.STRING), global = true)
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();
            interaction.respondLater();

            //We don't have to check if the optional is empty, because "subreddit" is a required command parameter
            String value = interaction.getArgumentStringValueByName("subreddit").get();
            String localeCode = interaction.getLocale().getLocaleCode();

            try {
                HttpResponse<String> init = Unirest.get(String.format(REDDIT_API_URL, URLEncoder.encode(value, StandardCharsets.UTF_8))).asString();

                switch (init.getStatus()) {
                    case 200: {
                        RedditSubmission submission = gson.fromJson(gson.fromJson(init.getBody(), JsonObject.class)
                                .get("data").getAsJsonObject()
                                .get("children").getAsJsonArray()
                                .get(0).getAsJsonObject()
                                .get("data").getAsJsonObject(), RedditSubmission.class);

                        EmbedBuilder embedBuilder = new EmbedBuilder()
                                .setAuthor(submission.author())
                                .setTitle(submission.title())
                                .setDescription(submission.selftext())
                                .setUrl("https://reddit.com" + submission.permalink())
                                .setFooter(String.format(Message.REDDIT_FOOTER.get(localeCode), submission.subreddit_name_prefixed()));

                        if (submission.url() != null && !submission.is_video()) embedBuilder.setImage(submission.url());

                        responder.addEmbed(embedBuilder).send();

                        if (submission.is_video())
                            interaction.createFollowupMessageBuilder()
                                    .addEmbed(new EmbedBuilder()
                                            .setDescription(Message.SUBMISSION_HAS_VIDEO.get(localeCode)))
                                    .send();
                        break;
                    }
                    case 403: {
                        responder.setContent(Message.SUBREDDIT_PRIVATE.get(localeCode)).send();
                        break;
                    }
                    case 404: {
                        responder.setContent(Message.SUBREDDIT_NOT_FOUND.get(localeCode)).send();
                        break;
                    }
                    default: {
                        responder.setContent(String.format(Message.REDDIT_ERROR.get(localeCode), init.getBody())).send();
                        break;
                    }
                }
            } catch (Exception ex) {
                responder.setContent(String.format(Message.ERROR.get(localeCode), ex.getMessage())).send();
                ex.printStackTrace();
            }
        }).start();
    }

    private record RedditSubmission(boolean over_18, boolean is_video, String permalink, String url, String selftext,
                                    String author, String title, String subreddit_name_prefixed) {
    }

}
