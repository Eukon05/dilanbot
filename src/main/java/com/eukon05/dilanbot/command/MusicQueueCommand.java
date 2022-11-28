package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import me.koply.kcommando.internal.OptionType;
import me.koply.kcommando.internal.annotations.HandleSlash;
import me.koply.kcommando.internal.annotations.Option;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

public class MusicQueueCommand extends AbstractMusicCommand {


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

            long p = interaction.getArgumentLongValueByName("page").orElse(1L);

            if (!voiceCheck(interaction))
                return;

            int queueSize = manager.getScheduler().getQueue().size();

            if (queueSize == 0) {
                responder.setContent("**The queue is empty!**").send();
                return;
            }

            int pages = queueSize / 5;

            if (pages == 0)
                pages = 1;

            else if (queueSize % 5 != 0)
                pages = pages + 1;


            if (p > pages) {
                responder.setContent("**:x: There aren't that many pages**").send();
                return;
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Tracks in the queue");
            embedBuilder.setFooter("Page " + p + "/" + pages);

            StringBuilder content = new StringBuilder();

            for (int i = (int) (5 * (p - 1)); i < 5 * p; i++) {
                if (i >= queueSize)
                    break;

                content.append(i + 1)
                        .append(".")
                        .append(" ")
                        .append("[")
                        .append(manager.getScheduler().getQueue().get(i).getInfo().title)
                        .append("](")
                        .append(manager.getScheduler().getQueue().get(i).getInfo().uri)
                        .append(")")
                        .append("\n");
            }

            embedBuilder.setDescription(content.toString());
            responder.addEmbed(embedBuilder).send();
        }).start();
    }

}
