package com.gotardpl.dilanbot.Configurations;

import com.gotardpl.dilanbot.Listeners.*;
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

    @Autowired
    DebugListener debugListener;

    @Autowired
    ServerJoinListenerImpl serverJoinListener;

    @Autowired
    PrefixListener prefixListener;

    @Autowired
    RedditListener redditListener;

    @Bean
    DiscordApi discordApi(){

        DiscordApi api = new DiscordApiBuilder()
                .setToken(discordToken)
                .setAllIntents()
                .login().join();

        api.addServerJoinListener(serverJoinListener);
        api.addMessageCreateListener(eightballListener);
        api.addMessageCreateListener(debugListener);
        api.addMessageCreateListener(prefixListener);
        api.addMessageCreateListener(redditListener);

        return api;
    }

}
