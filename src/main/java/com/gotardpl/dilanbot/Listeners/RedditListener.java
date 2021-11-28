package com.gotardpl.dilanbot.Listeners;

import com.gotardpl.dilanbot.DTOs.ServerDTO;
import com.gotardpl.dilanbot.Services.ServerService;
import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Submission;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedditListener implements MessageCreateListener {

    private final ServerService serverService;
    private final RedditClient redditClient;
    private final String keyWord = " reddit";

    @Autowired
    public RedditListener(ServerService serverService, RedditClient redditClient){
        this.serverService=serverService;
        this.redditClient=redditClient;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {

        ServerDTO serverDTO = serverService.getServerById(messageCreateEvent.getServer().get().getId());
        String message = messageCreateEvent.getMessageContent();
        ServerTextChannel channel = messageCreateEvent.getServerTextChannel().get();

        if(!message.startsWith(serverDTO.getPrefix() + keyWord))
            return;

        String subreddit = message.replaceFirst(serverDTO.getPrefix() + keyWord,"").trim();

        Submission submission = redditClient.subreddit(subreddit).randomSubmission().getSubject();

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
