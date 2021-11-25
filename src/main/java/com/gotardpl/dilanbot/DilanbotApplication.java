package com.gotardpl.dilanbot;

import org.javacord.api.DiscordApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DilanbotApplication {

    @Autowired
    public static void main(String[] args, DiscordApi api) {
        SpringApplication.run(DilanbotApplication.class, args);
        api.addMessageCreateListener()


    }

}
