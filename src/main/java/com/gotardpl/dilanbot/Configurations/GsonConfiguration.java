package com.gotardpl.dilanbot.Configurations;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfiguration {

    @Bean
    Gson gson(){
        return new Gson();
    }

}
