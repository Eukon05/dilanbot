package com.gotardpl.dilanbot.Configurations;

import com.gotardpl.dilanbot.Listeners.*;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;


@Configuration
public class DiscordConfiguration {

    @Value("${discord.token}")
    private String discordToken;

    @Autowired
    EightballMessageListener eightballMessageListener;

    @Autowired
    ServerJoinListenerImpl serverJoinListener;

    @Autowired
    PrefixMessageListener prefixMessageListener;

    @Autowired
    RedditMessageListener redditMessageListener;

    @Autowired
    MusicPlayMessageListener musicPlayMessageListener;

    @Autowired
    MusicStopMessageListener musicStopMessageListener;

    @Autowired
    MusicSkipMessageListener musicSkipMessageListener;

    @Autowired
    MusicPauseMessageListener musicPauseMessageListener;

    @Autowired
    VoiceChannelLeaveListener voiceChannelLeaveListener;

    @Autowired
    DisconnectMessageListener disconnectMessageListener;

    @Autowired MusicClearMessageListener musicClearMessageListener;

    @Autowired
    MusicLoopMessageListener musicLoopMessageListener;

    @Bean
    DiscordApi discordApi(){

        DiscordApi api = new DiscordApiBuilder()
                .setToken(discordToken)
                .setAllIntents()
                .login().join();

        api.addServerJoinListener(serverJoinListener);
        api.addMessageCreateListener(eightballMessageListener);
        api.addMessageCreateListener(prefixMessageListener);
        api.addMessageCreateListener(redditMessageListener);
        api.addMessageCreateListener(musicPlayMessageListener);
        api.addMessageCreateListener(musicStopMessageListener);
        api.addMessageCreateListener(musicSkipMessageListener);
        api.addMessageCreateListener(musicPauseMessageListener);
        api.addServerVoiceChannelMemberLeaveListener(voiceChannelLeaveListener);
        api.addMessageCreateListener(disconnectMessageListener);
        api.addMessageCreateListener(musicClearMessageListener);
        api.addMessageCreateListener(musicLoopMessageListener);

        return api;
    }

}
