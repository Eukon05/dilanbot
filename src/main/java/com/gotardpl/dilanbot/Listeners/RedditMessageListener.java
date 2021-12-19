package com.gotardpl.dilanbot.Listeners;

import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.models.Submission;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedditMessageListener extends AbstractMessageListener {

    private final RedditClient redditClient;

    @Autowired
    public RedditMessageListener(RedditClient redditClient){
        super(" reddit");
        this.redditClient = redditClient;

    }

    @Override
    void childOnMessageCreate(MessageCreateEvent event) {

        String subreddit = value;

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

        catch (Exception ex){
            channel.sendMessage("An unknown error has occurred: " + ex.getMessage());
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
