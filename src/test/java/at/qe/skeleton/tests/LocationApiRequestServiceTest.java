package at.qe.skeleton.tests;


import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.location.LocationDTO;
import at.qe.skeleton.external.repositories.LocationRepository;
import at.qe.skeleton.external.services.LocationApiRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LocationApiRequestServiceTest {
    @InjectMocks
    private LocationApiRequestService locationApiRequestService;

    @Mock
    private RestClient restClient;

    @Mock
    private LocationRepository locationRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRetrieveLocations_Success() throws Exception {
        // Arrange
        String cityName = "Innsbruck";
        int limit = 1;
        Map<String, String> localNames = new HashMap<>();
        LocationDTO locationDTO = new LocationDTO(
                "Innsbruck",
                localNames,
                47.0,
                13.0,
                "Austria",
                "Austria");


        ResponseEntity<List<LocationDTO>> responseEntity = ResponseEntity.ok(List.of(locationDTO));


        RestClient.RequestHeadersUriSpec requestHeadersUriSpecMock = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeadersSpecMock = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpecMock = mock(RestClient.ResponseSpec.class);

        when(restClient.get())
                .thenReturn(requestHeadersUriSpecMock);

        when(requestHeadersUriSpecMock.uri(anyString()))
                .thenReturn(requestHeadersSpecMock);

        when(requestHeadersSpecMock.retrieve())
                .thenReturn(responseSpecMock);


        when(responseSpecMock.toEntity(new ParameterizedTypeReference<List<LocationDTO>>() {
        }))
                .thenReturn(responseEntity);

        List<LocationDTO> result = locationApiRequestService.retrieveLocations(cityName, limit);

        assertFalse(result.isEmpty());
        assertEquals(result.get(0).name(), cityName);
        assertEquals(result.get(0).latitude(), 47.0);
        assertEquals(result.get(0).longitude(), 13.0);
        assertEquals(result.get(0).country(), "Austria");
    }


    @Test
    public void convertLocationDTOtoLocation() {
        Map<String, String> localNames = new HashMap<>();
        LocationDTO locationDTO = new LocationDTO(
                "Innsbruck",
                localNames,
                47.0,
                13.0,
                "Austria",
                "Austria");

        Location location = locationApiRequestService.convertLocationDTOtoLocation(locationDTO);
        when(locationRepository.save(location)).thenReturn(location);

        assertEquals(location.getName(), locationDTO.name());
        assertEquals(location.getLatitude(), locationDTO.latitude());
        assertEquals(location.getLongitude(), locationDTO.longitude());
        assertEquals(location.getCountry(), locationDTO.country());
    }



}
