package com.gotardpl.dilanbot.Configurations;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordConfiguration {

    @Value("discord.token")
    private String discordToken;

    @Bean
    DiscordApi discordApi(){
        return new DiscordApiBuilder().setToken(discordToken).login().join();
    }

}
