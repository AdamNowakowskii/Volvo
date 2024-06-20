package app.volvo.dto;

public record WeatherDTO(
        String date,
        double maxTemp,
        double minTemp,
        double avgTemp,
        double maxWind,
        double totalPrecipitation,
        int snowfall,
        int avgHumidity,
        int avgVisibility,
        int indexUV) {}
