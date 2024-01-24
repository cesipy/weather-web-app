package at.qe.skeleton.external.model.currentandforecast.misc.holiday;

import at.qe.skeleton.external.model.currentandforecast.misc.DailyTemperatureAggregationDTO;
import at.qe.skeleton.external.model.shared.WeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.CloudDTO;
import at.qe.skeleton.external.model.deserialisation.PrecipitationDeserializer;
import at.qe.skeleton.external.model.deserialisation.WeatherDeserializer;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
/**
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 * <br><br>
 * This class is used to model the answer to an API call
 *
 * @param latitude             Latitude of the location, decimal (−90; 90)
 * @param longitude            Longitude of the location, decimal (-180; 180)
 * @param timezone             Timezone in the ±XX:XX format
 * @param date                 Date specified in the API request in the `YYYY-MM-DD` format (from 1979-01-02 up to the 1,5 years ahead forecast)
 * @param units                Units of measurement specified in the reques
 * @param cloudDTO             Cloud related information
 * @param humidityDTO          Humidity related information
 * @param precipitationDTO     Precipitation related information
 * @param pressureDTO          Atmospheric pressure related information
 * @param temperatureDTO       Temperature related information
 * @param windDTO              Wind speed related information
 * @see <a href="https://openweathermap.org/api/one-call-3#current">API Documentation</a>
 */
public record HolidayDTO(
        @JsonProperty("lat") double latitude,
        @JsonProperty("lon") double longitude,
        @JsonProperty("tz") String timezone,
        @JsonProperty("date") Date date,
        @JsonProperty("units") String units,
        @JsonProperty("cloud_cover") CloudDTO cloudDTO,
        @JsonProperty("humidity") HumidityDTO humidityDTO,
        @JsonProperty("precipitation") PrecipitationDTO precipitationDTO,
        @JsonProperty("pressure") PressureDTO pressureDTO,
        @JsonProperty("temperature") DailyTemperatureAggregationDTO temperatureDTO,
        @JsonProperty("wind") WindDTO windDTO

) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;
    public String getFormattedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

}


