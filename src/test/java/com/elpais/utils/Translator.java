package com.elpais.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class Translator {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static final Logger LOGGER = LoggerFactory.getLogger(Translator.class);

    private Translator() {}

    // Uses Rapid Translate Multi Traduction API
    public static String translate(String text, String fromLang, String toLang) {
        try {
            if (text == null || text.isBlank()) return text;
            String from = (fromLang == null || fromLang.isBlank()) ? "auto" : fromLang;
            String to = (toLang == null || toLang.isBlank()) ? "en" : toLang;

            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String url = "https://rapid-translate-multi-traduction.p.rapidapi.com/t"
                       + "?text=" + encodedText
                       + "&from=" + from
                       + "&to=" + to;

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-RapidAPI-Key", "02aa05de3fmsh9205ad3c1ec25ecp1b89a2jsn6af83df3633e") // Replace with your RapidAPI key
                    .header("X-RapidAPI-Host", "rapid-translate-multi-traduction.p.rapidapi.com")
                    .GET()
                    .build();

            HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                JsonNode root = MAPPER.readTree(resp.body());
                JsonNode translatedText = root.path("translated_text");
                if (!translatedText.isMissingNode()) {
                    // Some responses may return an array, handle both cases
                    if (translatedText.isArray() && translatedText.size() > 0) {
                        return translatedText.get(0).asText();
                    }
                    return translatedText.asText();
                }
            }
            LOGGER.warn("Translation API unexpected response ({} -> {}, status {}): {}", from, to, resp.statusCode(), resp.body());
            return text;
        } catch (IOException | InterruptedException e) {
            LOGGER.warn("Translation failed for '{}' [{} -> {}]: {}", text, fromLang, toLang, e.getMessage());
            return text;
        }
    }

    public static String esToEn(String text) {
        return translate(text, "es", "en");
    }
}