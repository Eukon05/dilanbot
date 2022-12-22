package com.eukon05.dilanbot.command;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;

public interface Command {

    void run(SlashCommandCreateEvent event);

    default User getSelf(SlashCommandInteraction interaction) {
        return interaction.getApi().getYourself();
    }

    default Server getServer(SlashCommandInteraction interaction) {
        return interaction.getServer().get();
    }

}
