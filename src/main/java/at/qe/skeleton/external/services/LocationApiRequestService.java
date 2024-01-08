package at.qe.skeleton.external.services;

import at.qe.skeleton.external.model.location.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


/**
 * Service class for making requests to a location API to retrieve location information.
 * This class uses a `RestClient` for making HTTP requests to the location API.
 *
 * @see RestClient
 */
@Scope("application")
@Component
@Validated
public class LocationApiRequestService {

    private static final String CITY_NAME_PARAMETER = "q";

    private static final String GEOCODING_URI = "/geo/1.0/direct";

    private static final String LIMIT_PARAMETER = "limit";

    @Autowired
    private RestClient restClient;

    /**
     * Retrieves a list of location data based on the specified city name and limit.
     *
     * @param cityName name of city for which location information is requested.
     * @param limit    maximum number of results to be retrieved.
     * @return A list of {@link LocationDTO} objects representing the retrieved location data.
     */
    public List<LocationDTO> retrieveLocations(String cityName, int limit) {

        ResponseEntity<List<LocationDTO>> responseEntity = this.restClient.get()
                .uri(UriComponentsBuilder.fromPath(GEOCODING_URI)
                    .queryParam(CITY_NAME_PARAMETER, String.valueOf(cityName))
                    .queryParam(LIMIT_PARAMETER, String.valueOf(limit))
                    .build().toUriString())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<LocationDTO>>() {});

        return responseEntity.getBody();
    }
}
