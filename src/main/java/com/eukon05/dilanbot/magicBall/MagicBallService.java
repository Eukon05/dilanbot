package com.eukon05.dilanbot.magicBall;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class MagicBallService {
    private final Gson gson;
    private static final String EIGHTBALL_API_URL = "https://eightballapi.com/api?question=%s";
    private final HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();

    public MagicBallService(Gson gson) {
        this.gson = gson;
    }

    String getMagicBallResponse(String question) throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(String.format(EIGHTBALL_API_URL, URLEncoder.encode(question, StandardCharsets.UTF_8))))
                .build(), HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(response.body(), JsonObject.class).get("reading").getAsString();
    }

}
