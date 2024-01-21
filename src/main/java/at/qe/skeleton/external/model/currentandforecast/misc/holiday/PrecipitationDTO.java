package at.qe.skeleton.external.model.currentandforecast.misc.holiday;

import at.qe.skeleton.external.model.shared.WeatherDTO;
import at.qe.skeleton.external.model.deserialisation.PrecipitationDeserializer;
import at.qe.skeleton.external.model.deserialisation.WeatherDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * This class is used to model the answer to an API call
 *
 * @param total            Total amount of liquid water equivalent of precipitation for the date specified in the request
 */
public record PrecipitationDTO(
        @JsonProperty("total") int total

) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

}

