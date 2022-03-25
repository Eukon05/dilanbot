package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.Services.ServerService;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.listener.server.ServerJoinListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerJoinListenerImpl implements ServerJoinListener {

    @Value("${artifact.id})")
    private String artifactId;

    @Value("${artifact.version})")
    private String artifactVersion;

    private final ServerService serverService;

    @Autowired
    public ServerJoinListenerImpl(ServerService serverService){
        this.serverService=serverService;
    }

    @Override
    public void onServerJoin(ServerJoinEvent event) {

        if(serverService.getServerById(event.getServer().getId())==null)
            serverService.addServer(event.getServer().getId());

        try{
            event.getServer().getSystemChannel().ifPresent(channel -> {

                MessageBuilder msg = new MessageBuilder();
                EmbedBuilder emb = new EmbedBuilder();

                emb.setTitle("Thanks for adding me!");
                emb.setDescription("For a list of available commands, please visit our [GitHub page](https://https://github.com/Eukon05/dilanbot#features-and-usage)");
                emb.setThumbnail(event.getApi().getYourself().getAvatar());

                //I have no idea why the ")" symbol adds itself to the end of these values
                emb.setFooter(artifactId.replace(")","") +" version "+ artifactVersion.replace(")",""));

                msg.setEmbed(emb);
                msg.send(channel);

            });
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
