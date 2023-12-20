package at.qe.skeleton.external.model.location;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record LocationListDTO(
        List<LocationDTO> locations
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;
}
