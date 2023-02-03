package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.Message;
import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.lavaplayer.ServerMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.kcommando.internal.OptionType;
import me.koply.kcommando.internal.annotations.HandleSlash;
import me.koply.kcommando.internal.annotations.Option;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

import java.util.ArrayList;

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
            String localeCode = interaction.getLocale().getLocaleCode();

            ArrayList<AudioTrack> queue = manager.getScheduler().getQueue();
            long n = interaction.getArgumentLongValueByName("index").orElse((long) (queue.size()));

            if (!voiceCheck(interaction))
                return;

            if (queue.isEmpty()) {
                responder.setContent(Message.QUEUE_EMPTY.get(localeCode)).send();
                return;
            }

            if (n < 1) {
                responder.setContent(Message.QUEUE_INVALID_INDEX.get(localeCode)).send();
                return;
            }
            if (n > queue.size()) {
                responder.setContent(Message.QUEUE_LESS_TRACKS.get(localeCode)).send();
                return;
            }


            String title = queue.get((int) (n - 1)).getInfo().title;
            manager.getScheduler().getQueue().remove((int) n - 1);
            responder.setContent(String.format(Message.QUEUE_REMOVED_TRACK.get(localeCode), title)).send();
        }).start();
    }

}
