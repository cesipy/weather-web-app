package at.qe.skeleton.external.model.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LocationDTO(
    @JsonProperty("name") String name,
    @JsonProperty("lat") double latitude,
    @JsonProperty("lon") double longitude,
    @JsonProperty("country") String country,
    @JsonProperty("state") String state

) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;
}
