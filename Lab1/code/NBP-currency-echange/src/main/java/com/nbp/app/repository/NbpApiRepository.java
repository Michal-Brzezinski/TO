package com.nbp.app.repository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NbpApiRepository implements IRemoteRepository {
    @Override
    public byte[] get(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch data. HTTP Status: " + response.statusCode());
        }
        return response.body();
    }
}