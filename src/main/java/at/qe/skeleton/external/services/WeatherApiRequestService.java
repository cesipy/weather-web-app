package at.qe.skeleton.external.services;

import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.HolidayDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Scope("application")
@Component
@Validated // makes sure the parameter validation annotations are checked during runtime
public class WeatherApiRequestService {

    private static final String CURRENT_AND_FORECAST_URI = "/data/3.0/onecall";
    private static final String REVERSE_GEOCODING_URI = "/geo/1.0/reverse";
    private static final String DAILY_AGGREGATION_URI = "/data/3.0/onecall/day_summary";
    private static final String LONGITUDE_PARAMETER = "lon";
    private static final String LATITUDE_PARAMETER = "lat";
    private static final String DATE_PARAMETER = "date";

    private static final Logger logger = LoggerFactory.getLogger(WeatherApiRequestService.class);

    @Autowired
    private RestClient restClient;

    /**
     * Makes an API call to get the current and a weather forecast for a specified location
     * <br><br>
     * If you are unaware of lat/lon of the location use the geocoding api to determine those parameters
     * @param latitude of the location
     * @param longitude of the location
     * @return the current and forecast weather
     */
    public CurrentAndForecastAnswerDTO retrieveCurrentAndForecastWeather(@Min(-90) @Max(90) double latitude,
                                                                         @Min(-180) @Max(180) double longitude) throws ApiQueryException {
        try {
            ResponseEntity<CurrentAndForecastAnswerDTO> responseEntity = this.restClient.get()
                    .uri(UriComponentsBuilder.fromPath(CURRENT_AND_FORECAST_URI)
                            .queryParam(LATITUDE_PARAMETER, String.valueOf(latitude))
                            .queryParam(LONGITUDE_PARAMETER, String.valueOf(longitude))
                            .build().toUriString())
                    .retrieve()
                    .toEntity(CurrentAndForecastAnswerDTO.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            throw new ApiQueryException("Failed to retrieve weather data from API!");
        }
    }


    public HolidayDTO retrieveDailyHolidayForecast(@Min(-90) @Max(90) double latitude,
                                                   @Min(-180) @Max(180) double longitude,
                                                   String date){
        ResponseEntity<HolidayDTO> responseEntity = this.restClient.get()
                .uri(UriComponentsBuilder.fromPath(DAILY_AGGREGATION_URI)
                        .queryParam(LATITUDE_PARAMETER, String.valueOf(latitude))
                        .queryParam(LONGITUDE_PARAMETER, String.valueOf(longitude))
                        .queryParam(DATE_PARAMETER, date)
                        .build().toUriString())
                .retrieve()
                .toEntity(HolidayDTO.class);

        return responseEntity.getBody();

    }

}
