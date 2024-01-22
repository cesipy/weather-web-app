package at.qe.skeleton.external.model.currentandforecast.misc.holiday;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;


/**
 * This class is used to model the answer to an API call
 *
 * @param max            Maximum wind speed related information
 */
public record WindDTO(
        @JsonProperty("max") MaxDTO max

) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

}

