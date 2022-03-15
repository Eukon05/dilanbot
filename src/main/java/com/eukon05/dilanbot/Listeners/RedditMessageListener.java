package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.models.Submission;
import org.javacord.api.entity.channel.ServerTextChannel;
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
    void childOnMessageCreate(MessageCreateEvent event, ServerDTO serverDTO, String value) {

        new Thread(() -> {

            ServerTextChannel channel = event.getServerTextChannel().get();

            Submission submission;

            try {
                submission = redditClient.subreddit(value).randomSubmission().getSubject();
            }
            catch(NetworkException ex){

                if(ex.getRes().getCode()==404)
                    channel.sendMessage("This subreddit doesn't exist!");

                else
                    channel.sendMessage("An unknown HTTP error has occurred: " + ex.getMessage());

                ex.printStackTrace();
                return;
            }

            catch (ApiException ex){

                if(Integer.parseInt(ex.getCode())==403)
                    channel.sendMessage("This subreddit is private!");

                else
                    channel.sendMessage("An unknown API error has occurred: " + ex.getMessage());

                ex.printStackTrace();
                return;
            }

            catch (Exception ex){
                channel.sendMessage("An unknown error has occurred: " + ex.getMessage());

                ex.printStackTrace();
                return;
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
                    .setFooter("A random post from r/" + value);

            if(url!=null && url.contains("i.redd.it"))
                embedBuilder.setImage(url);

            builder.setEmbed(embedBuilder);
            builder.send(channel);

            System.out.println(submission.getPermalink());

        }).start();


    }

}
