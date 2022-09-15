package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.domain.RedditSubmission;
import com.eukon05.dilanbot.repository.CommandRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class RedditCommand extends Command {

    private final Gson gson;

    public RedditCommand(Gson gson, CommandRepository commandRepository) {
        super("reddit", commandRepository);
        this.gson = gson;
    }

    @Override
    public void run(MessageCreateEvent event, DiscordServer discordServer, String[] arguments) {
        new Thread(() -> {
            String value = fuseArguments(arguments);

            ServerTextChannel channel = event.getServerTextChannel().get();

            try {
                HttpResponse<String> init = Unirest
                        .get("https://www.reddit.com/r/" + URLEncoder.encode(value, StandardCharsets.UTF_8) + "/random.json")
                        .asString();

                switch (init.getStatus()) {
                    case 302 -> {
                        if (!init.getBody().isBlank()) {
                            channel.sendMessage("This subreddit is private");
                            return;
                        }
                    }
                    case 404 -> {
                        channel.sendMessage("This subreddit doesn't exist");
                        return;
                    }
                    default -> {
                        channel.sendMessage("An unexpected Reddit API error has occurred: " + init.getBody());
                        return;
                    }
                }

                URL url = new URL(init.getHeaders().get("location").get(0));

                String[] path = url.getPath().split("/");

                path[path.length - 2] = URLEncoder.encode(path[path.length - 2], StandardCharsets.UTF_8);

                HttpResponse<String> response = Unirest
                        .get("https://www.reddit.com/" + String.join("/", path))
                        .asString();

                RedditSubmission submission = gson
                        .fromJson(gson.fromJson(response.getBody(), JsonArray.class)
                                .get(0).getAsJsonObject().get("data")
                                .getAsJsonObject().get("children").getAsJsonArray().get(0).getAsJsonObject().get("data")
                                .getAsJsonObject(), RedditSubmission.class);

                if (submission.isOver_18() && !channel.isNsfw()) {
                    channel.sendMessage("This isn't a time and place for that, use an NSFW-enabled channel");
                    return;
                }

                MessageBuilder builder = new MessageBuilder();

                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setAuthor(submission.getAuthor())
                        .setTitle(submission.getTitle())
                        .setDescription(submission.getSelftext())
                        .setUrl("https://reddit.com" + submission.getPermalink())
                        .setFooter("A random post from " + submission.getSubreddit_name_prefixed());

                if (submission.getUrl() != null && !submission.is_video())
                    embedBuilder.setImage(submission.getUrl());

                builder.setEmbed(embedBuilder);
                builder.send(channel);

                if (submission.is_video())
                    new MessageBuilder()
                            .setEmbed(new EmbedBuilder()
                                    .setDescription("The above Reddit post contains a video, visit it in your browser to see it!"))
                            .send(channel);
            } catch (Exception ex) {
                channel.sendMessage("An unexpected backend error has occurred: " + ex.getMessage());
                ex.printStackTrace();
            }
        }).start();
    }

}
