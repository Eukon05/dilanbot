package com.eukon05.dilanbot.Configurations;

import core.GLA;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GLAConfiguration {

    @Bean
    GLA gla(){
        return new GLA();
    }

}
