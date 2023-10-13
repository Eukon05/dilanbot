package com.eukon05.dilanbot.reddit;

import com.eukon05.dilanbot.reddit.exception.SubredditNotFoundException;
import com.eukon05.dilanbot.reddit.exception.SubredditPrivateException;
import com.eukon05.dilanbot.reddit.exception.UnexpectedRedditException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class RedditService {
    private final Gson gson;
    private final HttpClient client = HttpClient.newHttpClient();
    private static final String REDDIT_API_URL = "https://www.reddit.com/r/%s/randomrising.json?limit=1";

    public RedditService(Gson gson) {
        this.gson = gson;
    }

    RedditSubmission getRandomSubmission(String subredditName) throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(HttpRequest
                .newBuilder()
                .uri(URI.create(String.format(REDDIT_API_URL, URLEncoder.encode(subredditName, StandardCharsets.UTF_8))))
                .build(), HttpResponse.BodyHandlers.ofString());

        return switch (response.statusCode()) {
            case 200 -> parseSubmission(response.body());
            case 403 -> throw new SubredditPrivateException();
            case 404 -> throw new SubredditNotFoundException();
            default -> throw new UnexpectedRedditException(response.body());
        };
    }

    private RedditSubmission parseSubmission(String responseBody) {
        return gson.fromJson(gson.fromJson(responseBody, JsonObject.class)
                .get("data").getAsJsonObject()
                .get("children").getAsJsonArray()
                .get(0).getAsJsonObject()
                .get("data").getAsJsonObject(), RedditSubmission.class);
    }

}
