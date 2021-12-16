package com.gotardpl.dilanbot.Listeners;

import com.gotardpl.dilanbot.DTOs.ServerDTO;
import com.gotardpl.dilanbot.Services.ServerService;
import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.models.Submission;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedditMessageListener implements MessageCreateListener {

    private final ServerService serverService;
    private final RedditClient redditClient;
    private final String keyWord = " reddit";

    @Autowired
    public RedditMessageListener(ServerService serverService, RedditClient redditClient){
        this.serverService=serverService;
        this.redditClient=redditClient;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        ServerDTO serverDTO = serverService.getServerById(event.getServer().get().getId());
        String message = event.getMessageContent();
        ServerTextChannel channel = event.getServerTextChannel().get();

        if(!message.startsWith(serverDTO.getPrefix() + keyWord))
            return;

        String subreddit = message.replaceFirst(serverDTO.getPrefix() + keyWord,"").trim();

        Submission submission;

        try {
             submission = redditClient.subreddit(subreddit).randomSubmission().getSubject();
        }
        catch(NetworkException ex){
            int status = ex.getRes().getCode();

            if(status==404)
                    channel.sendMessage("This subreddit doesn't exist!");

            else
                channel.sendMessage("An unknown HTTP error has occurred: " + ex.getMessage());


            ex.printStackTrace();
            return;
        }

        catch (ApiException ex){

            int status =  Integer.parseInt(ex.getCode());

            if(status==403)
                channel.sendMessage("This subreddit is private!");

            else
                channel.sendMessage("An unknown API error has occurred: " + ex.getMessage());

            ex.printStackTrace();
            return;
        }

        boolean validPost = false;
        while(!validPost){

            if(!(submission.getSelfText()!=null && submission.getUrl().contains("v.redd.it")))
                validPost=true;

        }

        String url = submission.getUrl();


        if(submission.isNsfw() && !channel.isNsfw()){
            channel.sendMessage("This isn't a time and place for that, use an NSFW-enabled channel");
            return;
        }

        MessageBuilder builder = new MessageBuilder();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(submission.getAuthor())
                .setTitle(submission.getTitle())
                .setDescription(submission.getSelfText())
                .setUrl("https://reddit.com" + submission.getPermalink())
                .setFooter("A random post from r/" + subreddit);


        if(url!=null && url.contains("i.redd.it"))
            embedBuilder.setImage(url);

        builder.setEmbed(embedBuilder);
        builder.send(channel);

        System.out.println(submission.getPermalink());


    }

}