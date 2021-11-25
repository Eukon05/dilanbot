package com.gotardpl.dilanbot.Configurations;

import com.gotardpl.dilanbot.Listeners.EightballListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DiscordConfiguration {

    @Value("${discord.token}")
    private String discordToken;

    @Autowired
    EightballListener eightballListener;

    @Bean
    DiscordApi discordApi(){

        DiscordApi api = new DiscordApiBuilder()
                .setToken(discordToken)
                .login().join();

        api.addMessageCreateListener(eightballListener);

        return api;
    }

}
