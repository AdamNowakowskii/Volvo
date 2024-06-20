package app.volvo.dto;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class WeatherDeserializer extends JsonDeserializer<WeatherDaysDTO> {

    @Override
    public WeatherDaysDTO deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode forecastDaysNode = node.path("forecast").path("forecastday");
        Set<WeatherDTO> weatherDays = new LinkedHashSet<>();

        for (JsonNode dayNode : forecastDaysNode) {
            JsonNode dayDataNode = dayNode.path("day");
            WeatherDTO weatherDTO = new WeatherDTO(
                    dayNode.path("date").asText(),
                    dayDataNode.path("maxtemp_c").asDouble(),
                    dayDataNode.path("mintemp_c").asDouble(),
                    dayDataNode.path("avgtemp_c").asDouble(),
                    dayDataNode.path("maxwind_kph").asDouble(),
                    dayDataNode.path("totalprecip_mm").asDouble(),
                    dayDataNode.path("totalsnow_cm").asInt(),
                    dayDataNode.path("avghumidity").asInt(),
                    dayDataNode.path("avgvis_km").asInt(),
                    dayDataNode.path("uv").asInt());

            weatherDays.add(weatherDTO);
        }

        return new WeatherDaysDTO(weatherDays);
    }

}
