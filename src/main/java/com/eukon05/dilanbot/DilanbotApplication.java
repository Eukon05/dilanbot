package com.eukon05.dilanbot;

import com.eukon05.dilanbot.Lavaplayer.PlayerManager;
import org.javacord.api.DiscordApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DilanbotApplication {

    @Autowired
    public DilanbotApplication(PlayerManager playerManager, DiscordApi api){
        playerManager.api=api;
    }

    public static void main(String[] args) {
        SpringApplication.run(DilanbotApplication.class, args);
    }



}
