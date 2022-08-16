package com.eukon05.dilanbot.configuration;

import kong.unirest.Unirest;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnirestConfiguration {

    public UnirestConfiguration() {
        Unirest.config().followRedirects(false);
    }

}
