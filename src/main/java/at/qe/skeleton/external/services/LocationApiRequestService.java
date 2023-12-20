package at.qe.skeleton.external.services;

import at.qe.skeleton.external.model.location.LocationListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Scope("application")
@Component
@Validated
public class LocationApiRequestService {

    private static final String CITY_NAME_PARAMETER = "q";

    private static final String GEOCODING_URI = "/geo/1.0/direct";

    @Autowired
    private RestClient restClient;

    public LocationListDTO retrieveLocations(String cityName) {

        ResponseEntity<LocationListDTO> responseEntity = this.restClient.get()
                .uri(UriComponentsBuilder.fromPath(GEOCODING_URI)
                    .queryParam(CITY_NAME_PARAMETER, String.valueOf(cityName))
                    .build().toUriString())
                .retrieve()
                .toEntity(LocationListDTO.class);

        return responseEntity.getBody();
    }
}
