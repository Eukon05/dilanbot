package com.eukon05.dilanbot;

import com.eukon05.dilanbot.listener.ServerJoinListenerImpl;
import com.eukon05.dilanbot.listener.VoiceChannelLeaveListener;
import com.eukon05.dilanbot.magicBall.MagicBallCommandHandler;
import com.eukon05.dilanbot.magicBall.MagicBallService;
import com.eukon05.dilanbot.music.MusicCommandHandler;
import com.eukon05.dilanbot.music.MusicService;
import com.eukon05.dilanbot.reddit.RedditCommandHandler;
import com.eukon05.dilanbot.reddit.RedditService;
import com.google.gson.Gson;
import me.koply.kcommando.KCommando;
import me.koply.kcommando.integration.impl.javacord.JavacordIntegration;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.util.Optional;

class DilanbotApplication {
    public static void main(String[] args) {
        Optional<String> discordToken = Optional.ofNullable(System.getenv("DILAN_TOKEN"));

        if (discordToken.isEmpty()) {
            System.err.println("Discord token not present! Make sure to set DILAN_TOKEN environment variable correctly!");
            System.exit(401);
        }

        Gson gson = new Gson();
        MusicService musicService = new MusicService();

        DiscordApi api = new DiscordApiBuilder()
                .setToken(discordToken.get())
                .login()
                .join();

        JavacordIntegration integration = new JavacordIntegration(api);
        KCommando kCommando = new KCommando(integration)
                .addPackage("com.eukon05.dilanbot.music")
                .addPackage("com.eukon05.dilanbot.reddit")
                .addPackage("com.eukon05.dilanbot.magicBall")// package to analyze
                .setCooldown(5000L) // 5 seconds as 5000 ms
                .setVerbose(true) // for logging
                .build();

        api.addServerJoinListener(new ServerJoinListenerImpl());
        api.addServerVoiceChannelMemberLeaveListener(new VoiceChannelLeaveListener(musicService));

        kCommando.registerObject(new MagicBallCommandHandler(new MagicBallService(gson)));
        kCommando.registerObject(new RedditCommandHandler(new RedditService(gson)));
        kCommando.registerObject(new MusicCommandHandler(musicService));
    }
}
