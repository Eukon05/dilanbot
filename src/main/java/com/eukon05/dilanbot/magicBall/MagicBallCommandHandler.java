package com.eukon05.dilanbot.magicBall;

import com.eukon05.dilanbot.Message;
import me.koply.kcommando.internal.OptionType;
import me.koply.kcommando.internal.annotations.HandleSlash;
import me.koply.kcommando.internal.annotations.Option;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

import java.io.IOException;

public class MagicBallCommandHandler {

    private final MagicBallService service;
    private static final String ARGUMENT = "question";

    public MagicBallCommandHandler(MagicBallService service) {
        this.service = service;
    }

    @HandleSlash(name = "8ball", desc = "Ask the 8ball a question!", options = @Option(name = ARGUMENT, required = true, type = OptionType.STRING), global = true)
    public void handleMagicBallCommand(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        interaction.respondLater();

        InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();

        String question = interaction.getArgumentStringValueByName(ARGUMENT).orElseThrow();

        String locale = interaction.getLocale().getLocaleCode();

        User user = interaction.getUser();

        try {
            String response = service.getMagicBallResponse(question);
            EmbedBuilder embed = buildEmbedForResponse(question, response, user, locale);

            responder.addEmbed(embed);

        } catch (IOException | InterruptedException e) {
            responder.setContent(String.format(Message.ERROR.get(locale), e.getMessage())).send();
            e.printStackTrace();
        } finally {
            responder.send();
        }
    }

    private EmbedBuilder buildEmbedForResponse(String question, String response, User user, String locale) {
        return new EmbedBuilder()
                .setAuthor(user)
                .setTitle(question)
                .setDescription(response)
                .setFooter(Message.EIGHTBALL_FOOTER.get(locale));
    }
}
