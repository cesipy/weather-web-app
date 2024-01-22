package at.qe.skeleton.external.model.currentandforecast.misc.holiday;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

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

