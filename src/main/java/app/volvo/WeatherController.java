package app.volvo;

import static java.net.http.HttpClient.newHttpClient;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.volvo.dto.WeatherDaysDTO;
import app.volvo.exception.APIKeyDisabledException;
import app.volvo.exception.APIQuotaExceededException;
import lombok.RequiredArgsConstructor;

/**
 * Gdyby ta aplikacja miała służyć nie tylko celom rekrutacyjnym, to:
 * API_KEY byłby ukryty (np. ustawiony jako zmienne środowiskowe), a nie zapisany na sztywno w kodzie
 * Do wysyłania zapytań  użyłbym Bulka, jednak jest on dostępny wyłącznie w płatnych planach API
 */

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private static final String API_KEY = "ee0f90c11c2f4dc4b9d94651241806";
    private static final String FORECAST_REQUEST_URI = "https://api.weatherapi.com/v1/forecast.json";

    private final HttpClient httpClient = newHttpClient();
    private final ObjectMapper objectMapper;

    public enum City {
        WARSAW, CRACOW, WROCLAW, LODZ, POZNAN
    }

    @GetMapping(value = "/forecast", produces = APPLICATION_JSON_VALUE)
    public Map<City, WeatherDaysDTO> getForecast() throws IOException, InterruptedException {
        Map<City, WeatherDaysDTO> forecast = new HashMap<>();

        for (City city : City.values()) {
            HttpResponse<String> response = sendForecastRequest(city, httpClient);

            if (response.statusCode() != 200)
                handleErrorResponse(response);

            forecast.put(city, objectMapper.readValue(response.body(), WeatherDaysDTO.class));
        }

        return forecast;
    }

    private static HttpResponse<String> sendForecastRequest(
            City city, HttpClient httpClient) throws IOException, InterruptedException {
        URI uri = fromHttpUrl(FORECAST_REQUEST_URI)
                .queryParam("q", city)
                .queryParam("days", "3")
                .queryParam("key", API_KEY)
                .build().toUri();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        return httpClient.send(request, BodyHandlers.ofString());
    }

    private void handleErrorResponse(HttpResponse<String> response) throws IOException {
        int errorCode = objectMapper.readTree(response.body()).path("error").path("code").asInt();

        switch (errorCode) {
            case 2007 -> throw new APIQuotaExceededException("API key has exceeded calls per month quota");
            case 2008 -> throw new APIKeyDisabledException("API key has expired");
            default -> throw new RuntimeException();
        }
    }

}

