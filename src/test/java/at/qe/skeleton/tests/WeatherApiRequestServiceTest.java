package at.qe.skeleton.tests;

import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.HolidayDTO;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WeatherApiRequestServiceTest {
    @InjectMocks
    private WeatherApiRequestService weatherApiRequestService;

    @Mock
    private RestClient restClient;



    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRetrieveCurrentAndForecastWeather_Success() throws Exception {
        // Arrange
        double latitude = 47.0;
        double longitude = 13.0;

        CurrentAndForecastAnswerDTO expectedResponse = new CurrentAndForecastAnswerDTO(
                47.0,
                13.0,
                "Vienna",
                3,
                null,
                null,
                null,
                null,
                null
        );

        RestClient.RequestHeadersUriSpec requestHeadersUriSpecMock = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeadersSpecMock = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpecMock = mock(RestClient.ResponseSpec.class);

        when(restClient.get())
                .thenReturn(requestHeadersUriSpecMock);

        when(requestHeadersUriSpecMock.uri(anyString()))
                .thenReturn(requestHeadersSpecMock);

        when(requestHeadersSpecMock.retrieve())
                .thenReturn(responseSpecMock);

        when(responseSpecMock.toEntity(eq(CurrentAndForecastAnswerDTO.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        CurrentAndForecastAnswerDTO result = weatherApiRequestService.retrieveCurrentAndForecastWeather(latitude, longitude);

        assertEquals(expectedResponse, result);
    }


    @Test
    public void testRetrieveDailyHolidayForecast_Success() {
        // Arrange
        double latitude = 47.0;
        double longitude = 13.0;
        String date = "2024-01-20";

        // Create a sample HolidayDTO response
        HolidayDTO expectedHolidayDTO = new HolidayDTO(
                47.0,
                13.0,
                null,
                null,
                "meter",
                null,
                null, null, null, null, null
        );

        // Mock the RestClient behavior
        RestClient.RequestHeadersUriSpec requestHeadersUriSpecMock = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeadersSpecMock = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpecMock = mock(RestClient.ResponseSpec.class);

        when(restClient.get())
                .thenReturn(requestHeadersUriSpecMock);

        when(requestHeadersUriSpecMock.uri(anyString()))
                .thenReturn(requestHeadersSpecMock);

        when(requestHeadersSpecMock.retrieve())
                .thenReturn(responseSpecMock);

        // Mock the behavior of responseSpecMock
        ResponseEntity<HolidayDTO> responseEntityMock = ResponseEntity.ok(expectedHolidayDTO);
        when(responseSpecMock.toEntity(HolidayDTO.class))
                .thenReturn(responseEntityMock);

        // Act
        HolidayDTO result = weatherApiRequestService.retrieveDailyHolidayForecast(latitude, longitude, date);

        // Assert
        assertEquals(expectedHolidayDTO, result);
    }

}
