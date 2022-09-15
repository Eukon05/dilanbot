package com.eukon05.dilanbot.configuration;

import com.eukon05.dilanbot.listener.MessageListener;
import com.eukon05.dilanbot.listener.ServerJoinListenerImpl;
import com.eukon05.dilanbot.listener.VoiceChannelLeaveListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DiscordConfiguration {

    @Value("${discord.token}")
    private String discordToken;

    @Bean
    DiscordApi discordApi(MessageListener messageListener, ServerJoinListenerImpl serverJoinListener, VoiceChannelLeaveListener vcLeaveListener) {
        DiscordApi api = new DiscordApiBuilder()
                .setToken(discordToken)
                .setAllIntents()
                .login()
                .join();

        api.addMessageCreateListener(messageListener);
        api.addServerJoinListener(serverJoinListener);
        api.addServerVoiceChannelMemberLeaveListener(vcLeaveListener);
        return api;
    }

}
