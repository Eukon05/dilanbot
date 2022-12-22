package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.MessageUtils;
import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.koply.kcommando.internal.OptionType;
import me.koply.kcommando.internal.annotations.HandleSlash;
import me.koply.kcommando.internal.annotations.Option;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

import java.util.ArrayList;

public class MusicQueueCommand extends AbstractMusicCommand {

    private static final String QUEUE_LISTING = "%d. [%s](%s) \n";
    private static final String QUEUE_PAGE = "Page %d/%d";

    public MusicQueueCommand(PlayerManager playerManager) {
        super(playerManager);
    }

    @HandleSlash(name = "queue",
            desc = "Lists tracks that are currently queued",
            options = @Option(name = "page", desc = "Which page of the queue to display", type = OptionType.INTEGER),
            global = true)
    @Override
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            interaction.respondLater();
            ServerMusicManager manager = playerManager.getServerMusicManager(getServer(interaction).getId());
            InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();
            String localeCode = interaction.getLocale().getLocaleCode();

            ArrayList<AudioTrack> queue = manager.getScheduler().getQueue();
            long p = interaction.getArgumentLongValueByName("page").orElse(1L);

            if (!voiceCheck(interaction))
                return;

            int queueSize = queue.size();

            if (queueSize == 0) {
                responder.setContent(MessageUtils.getMessage("QUEUE_EMPTY", localeCode)).send();
                return;
            }

            int pages = queueSize / 5;

            if (pages == 0)
                pages = 1;

            else if (queueSize % 5 != 0)
                pages = pages + 1;


            if (p > pages) {
                responder.setContent(MessageUtils.getMessage("QUEUE_LESS_PAGES", localeCode)).send();
                return;
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(MessageUtils.getMessage("QUEUE_TRACKS", localeCode));
            embedBuilder.setFooter(String.format(QUEUE_PAGE, p, pages));

            StringBuilder content = new StringBuilder();

            AudioTrackInfo trackInfo;
            for (int i = (int) (5 * (p - 1)); i < 5 * p; i++) {
                if (i >= queueSize)
                    break;

                trackInfo = queue.get(i).getInfo();
                content.append(String.format(QUEUE_LISTING, i + 1, trackInfo.title, trackInfo.uri));
            }

            embedBuilder.setDescription(content.toString());
            responder.addEmbed(embedBuilder).send();
        }).start();
    }

}
