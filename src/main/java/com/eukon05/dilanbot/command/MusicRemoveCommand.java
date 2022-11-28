package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import me.koply.kcommando.internal.OptionType;
import me.koply.kcommando.internal.annotations.HandleSlash;
import me.koply.kcommando.internal.annotations.Option;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

public class MusicRemoveCommand extends AbstractMusicCommand {


    public MusicRemoveCommand(PlayerManager playerManager) {
        super(playerManager);
    }

    @HandleSlash(name = "remove",
            desc = "Removes a track from the queue",
            options = @Option(name = "index", desc = "number of the song in the queue to remove", type = OptionType.INTEGER),
            global = true)
    @Override
    public void run(SlashCommandCreateEvent event) {
        new Thread(() -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            interaction.respondLater();
            ServerMusicManager manager = playerManager.getServerMusicManager(event.getSlashCommandInteraction().getServer().get().getId());
            InteractionFollowupMessageBuilder responder = interaction.createFollowupMessageBuilder();

            long n = interaction.getArgumentLongValueByName("index").orElse(1L);

            if (!voiceCheck(interaction))
                return;

            int queueSize = manager.getScheduler().getQueue().size();

            if (queueSize == 0) {
                responder.setContent("**The queue is empty!**").send();
                return;
            }


            if (n < 1) {
                responder.setContent("**The index of the song to remove must be equal or greater than 1**").send();
                return;
            }
            if (n > queueSize) {
                responder.setContent("**There are less songs in the queue than the index you've specified**").send();
                return;
            }


            String title = manager.getScheduler().getQueue().get((int) (n - 1)).getInfo().title;
            manager.getScheduler().getQueue().remove((int) n - 1);
            responder.setContent("**Removed \"" + title + "\" from the queue**").send();
        }).start();
    }

}
