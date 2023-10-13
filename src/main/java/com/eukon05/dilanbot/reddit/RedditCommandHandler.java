package com.eukon05.dilanbot.reddit;

import com.eukon05.dilanbot.DilanException;
import com.eukon05.dilanbot.Message;
import me.koply.kcommando.internal.OptionType;
import me.koply.kcommando.internal.annotations.HandleSlash;
import me.koply.kcommando.internal.annotations.Option;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

import java.io.IOException;

public class RedditCommandHandler {
    private final RedditService service;
    private static final String ARGUMENT = "subreddit";
    private static final String REDDIT_URL = "https://reddit.com%s";

    public RedditCommandHandler(RedditService service) {
        this.service = service;
    }

    @HandleSlash(name = "reddit", desc = "Fetches a random submission from the given subreddit", global = true, options = @Option(name = ARGUMENT, required = true, type = OptionType.STRING))
    public void handleRedditCommand(SlashCommandCreateEvent event) throws IOException, InterruptedException {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        String locale = interaction.getLocale().getLocaleCode();
        interaction.respondLater();

        InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();

        String subName = interaction.getArgumentStringValueByName(ARGUMENT).orElseThrow();

        try {
            RedditSubmission submission = service.getRandomSubmission(subName);
            responder.addEmbed(buildEmbedFromSubmission(submission, locale)).send();

            if (submission.is_video())
                interaction.createFollowupMessageBuilder()
                        .addEmbed(new EmbedBuilder().setDescription(Message.SUBMISSION_HAS_VIDEO.get(locale)))
                        .send();
        } catch (DilanException e) {
            e.handle(responder, locale);
        }
    }

    private EmbedBuilder buildEmbedFromSubmission(RedditSubmission submission, String locale) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setAuthor(submission.author())
                .setDescription(submission.selftext())
                .setTitle(submission.title())
                .setUrl(String.format(REDDIT_URL, submission.permalink()))
                .setFooter(String.format(Message.REDDIT_FOOTER.get(locale), submission.subreddit_name_prefixed()));

        if (submission.url() != null && !submission.is_video() && submission.url().contains("i.redd.it"))
            embed.setImage(submission.url());

        return embed;
    }
}
