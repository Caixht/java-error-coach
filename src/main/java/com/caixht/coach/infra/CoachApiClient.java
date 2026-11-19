package com.caixht.coach.infra;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class CoachApiClient {

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public ApiResponse post(String url, String jsonBody) throws IOException, InterruptedException {
        return post(url, jsonBody, null);
    }

    public ApiResponse post(String url, String jsonBody, String apiKey)
            throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8));

        if (apiKey != null && !apiKey.isBlank()) {
            builder.header("Authorization", "Bearer " + apiKey);
        }

        HttpResponse<String> response =
                http.send(builder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        return new ApiResponse(response.statusCode(), response.body());
    }
}