package com.gotardpl.dilanbot.Configurations;


import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedditConfiguration {

        @Value("${reddit.username}")
        private String redditUsername;

        @Value("${reddit.password}")
        private String redditPassword;

        @Value("${reddit.clientid}")
        private String redditClientid;
        @Value("${reddit.clientsecret}")

        private String redditClientSecret;
        @Value("${artifact.id}")
        private String artifactId;

        @Value("${artifact.version}")
        private String artifactVersion;

        @Bean
        RedditClient redditClient(){

                UserAgent userAgent = new UserAgent("bot", artifactId, artifactVersion, redditUsername);

                Credentials credentials = Credentials.script(redditUsername, redditPassword,
                        redditClientid, redditClientSecret);

                NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);

                return OAuthHelper.automatic(adapter, credentials);

        }


}


