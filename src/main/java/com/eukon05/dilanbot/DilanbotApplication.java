package com.eukon05.dilanbot;

import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import org.javacord.api.DiscordApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DilanbotApplication {

    public DilanbotApplication(PlayerManager playerManager, DiscordApi api) {
        playerManager.setApi(api);
    }

    public static void main(String[] args) {
        SpringApplication.run(DilanbotApplication.class, args);
    }
}
