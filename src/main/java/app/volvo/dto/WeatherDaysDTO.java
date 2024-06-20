package app.volvo.dto;

import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = WeatherDeserializer.class)
public record WeatherDaysDTO(Set<WeatherDTO> weatherDays) {}
