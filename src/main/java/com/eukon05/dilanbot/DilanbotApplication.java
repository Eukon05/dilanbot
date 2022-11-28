package com.eukon05.dilanbot;

import com.eukon05.dilanbot.command.*;
import com.eukon05.dilanbot.lavaplayer.PlayerManager;
import com.eukon05.dilanbot.listener.ServerJoinListenerImpl;
import com.eukon05.dilanbot.listener.VoiceChannelLeaveListener;
import com.google.gson.Gson;
import core.GLA;
import kong.unirest.Unirest;
import me.koply.kcommando.KCommando;
import me.koply.kcommando.integration.impl.javacord.JavacordIntegration;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.net.MalformedURLException;

public class DilanbotApplication {

    public static void main(String[] args) throws MalformedURLException {
        String discordToken = System.getenv("DILAN_TOKEN");

        if (discordToken == null) {
            System.err.println("Discord token not present! Make sure to set DILAN_TOKEN environment variable correctly!");
            System.exit(401);
        }

        String wordListUrl = System.getenv("DILAN_WORDLIST");
        if (wordListUrl == null) {
            wordListUrl = "https://github.com/Eukon05/dilanbot/raw/master/lyrics-wordlist.json";
        } else {
            System.out.println("Custom lyrics wordlist URL detected");
        }

        Unirest.config().followRedirects(false);


        GLA gla = new GLA();
        Gson gson = new Gson();

        DiscordApi api = new DiscordApiBuilder()
                .setToken(discordToken)
                .setAllIntents()
                .login()
                .join();

        PlayerManager playerManager = new PlayerManager(api);

        api.addServerJoinListener(new ServerJoinListenerImpl());
        api.addServerVoiceChannelMemberLeaveListener(new VoiceChannelLeaveListener(playerManager));

        JavacordIntegration integration = new JavacordIntegration(api);
        KCommando kCommando = new KCommando(integration)
                .addPackage("com.eukon05.dilanbot.command") // package to analyze
                .setCooldown(5000L) // 5 seconds as 5000 ms
                .setVerbose(true) // for logging
                .build();

        kCommando.registerObject(new EightballCommand(gson));
        kCommando.registerObject(new RedditCommand(gson));
        kCommando.registerObject(new MusicPlayCommand(playerManager));
        kCommando.registerObject(new MusicPauseCommand(playerManager));
        kCommando.registerObject(new MusicStopCommand(playerManager));
        kCommando.registerObject(new MusicSkipCommand(playerManager));
        kCommando.registerObject(new MusicClearCommand(playerManager));
        kCommando.registerObject(new MusicLoopCommand(playerManager));
        kCommando.registerObject(new MusicQueueCommand(playerManager));
        kCommando.registerObject(new MusicRemoveCommand(playerManager));
        kCommando.registerObject(new MusicShuffleCommand(playerManager));
        kCommando.registerObject(new MusicNowPlayingCommand(playerManager));
        kCommando.registerObject(new MusicLyricsCommand(gla, gson, wordListUrl, playerManager));
        kCommando.registerObject(new DisconnectCommand(playerManager));
    }

}
