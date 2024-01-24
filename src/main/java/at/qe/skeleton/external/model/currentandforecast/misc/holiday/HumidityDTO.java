package at.qe.skeleton.external.model.currentandforecast.misc.holiday;


import com.fasterxml.jackson.annotation.JsonProperty;


import java.io.Serial;
import java.io.Serializable;


/**
 * This class is used to model the answer to an API call
 *
 * @param afternoon            Relative humidity in percent at 12:00 for the date specified in the request
 */
public record HumidityDTO(
        @JsonProperty("afternoon") int afternoon

) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

}

