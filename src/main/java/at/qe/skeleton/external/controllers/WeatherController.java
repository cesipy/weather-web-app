package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller         // @Controller is a specification of @Component
@Scope("view")
public class WeatherController {
    private double latitude;

    private double longitude;

    private String currentWeather;

    private CurrentAndForecastAnswerDTO currentWeatherDTO;

    @Autowired
    private WeatherApiRequestService weatherApiRequestService;

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherController.class);

    public void requestWeather() {
        try {
            CurrentAndForecastAnswerDTO answer = this.weatherApiRequestService.retrieveCurrentAndForecastWeather(getLatitude(), getLongitude());
            setCurrentWeatherDTO(answer);

            ObjectMapper mapper = new ObjectMapper()
                    .findAndRegisterModules()
                    .enable(SerializationFeature.INDENT_OUTPUT);
            String plainTextAnswer = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(answer);
            String escapedHtmlAnswer = StringEscapeUtils.escapeHtml4(plainTextAnswer);
            String escapedHtmlAnswerWithLineBreaks = escapedHtmlAnswer.replace("\n", "<br>")
                    .replace(" ", "&nbsp;");
            this.setCurrentWeather(escapedHtmlAnswerWithLineBreaks);

        } catch (final Exception e) {
            LOGGER.error("error in request", e);
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(String currentWeather) {
        this.currentWeather = currentWeather;
    }

    public CurrentAndForecastAnswerDTO getCurrentWeatherDTO() {
        return currentWeatherDTO;
    }

    public void setCurrentWeatherDTO(CurrentAndForecastAnswerDTO currentWeatherDTO) {
        this.currentWeatherDTO = currentWeatherDTO;
    }
}