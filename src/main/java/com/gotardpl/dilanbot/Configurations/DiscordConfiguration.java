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
    ServerJoinListenerImpl serverJoinListener;

    @Autowired
    PrefixListener prefixListener;

    @Autowired
    RedditListener redditListener;

    @Autowired
    MusicPlayListener musicPlayListener;

    @Autowired
    MusicStopListener musicStopListener;

    @Autowired
    MusicSkipListener musicSkipListener;

    @Autowired
    MusicPauseListener musicPauseListener;

    @Bean
    DiscordApi discordApi(){

        DiscordApi api = new DiscordApiBuilder()
                .setToken(discordToken)
                .setAllIntents()
                .login().join();

        api.addServerJoinListener(serverJoinListener);
        api.addMessageCreateListener(eightballListener);
        api.addMessageCreateListener(prefixListener);
        api.addMessageCreateListener(redditListener);
        api.addMessageCreateListener(musicPlayListener);
        api.addMessageCreateListener(musicStopListener);
        api.addMessageCreateListener(musicSkipListener);
        api.addMessageCreateListener(musicPauseListener);

        return api;
    }

}
