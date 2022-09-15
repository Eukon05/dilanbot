package com.eukon05.dilanbot.command;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.repository.CommandRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Component
public class EightballCommand extends Command {

    private final Gson gson;

    public EightballCommand(CommandRepository commandRepository, Gson gson) {
        super("8ball", commandRepository);
        this.gson = gson;
    }

    @Override
    public void run(MessageCreateEvent event, DiscordServer discordServer, String[] arguments) {
        new Thread(() -> {
            String value = fuseArguments(arguments);

            ServerTextChannel channel = event.getServerTextChannel().get();

            try {

                HttpResponse<String> response = Unirest
                        .get("https://8ball.delegator.com/magic/JSON/" + URLEncoder.encode(value, StandardCharsets.UTF_8))
                        .asString();
                JsonObject responseJson = gson.fromJson(response.getBody(), JsonObject.class).get("magic").getAsJsonObject();

                new MessageBuilder().setEmbed(new EmbedBuilder()
                        .setTitle("The Magic 8-Ball says:")
                        .setDescription(responseJson.get("answer").getAsString())
                        .setFooter("Powered by Delegator")
                ).send(channel);

            } catch (JsonSyntaxException ex) {
                channel.sendMessage("Something went wrong: Question contains invalid characters");
                ex.printStackTrace();
            } catch (Exception ex) {
                channel.sendMessage("Something went wrong: " + ex.getMessage());
                ex.printStackTrace();
            }
        }).start();
    }

}
