package at.qe.skeleton.external.model.currentandforecast.misc.holiday;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;


/**
 * This class is used to model the answer to an API call
 *
 * @param speed            Maximum wind speed for the date specified in the request
 */
public record MaxDTO(
        @JsonProperty("speed") double speed,
        @JsonProperty("direction") int direction

) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

}

