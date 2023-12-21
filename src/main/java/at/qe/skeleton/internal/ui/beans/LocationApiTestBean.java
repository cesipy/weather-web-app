package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.location.LocationDTO;
import at.qe.skeleton.external.services.LocationApiRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component
@Scope("view")
public class LocationApiTestBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationApiTestBean.class);

    @Autowired
    private LocationApiRequestService locationApiRequestService;

    private String currentLocation;
    private final String QUERY_NAME = "Innsbruck";            // hard coded name for query test
    private final int LIMIT = 1;


    @PostConstruct
    public void init() {
        try {
            List<LocationDTO> answer = this.locationApiRequestService.retrieveLocations(getQUERY_NAME(), getLIMIT());

            // TODO: handle each element of list using for loop
            ObjectMapper mapper = new ObjectMapper()
                    .findAndRegisterModules()
                    .enable(SerializationFeature.INDENT_OUTPUT);

            String plainTextAnswer = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(answer);
            String escapedHtmlAnswer = StringEscapeUtils.escapeHtml4(plainTextAnswer);
            String escapedHtmlAnswerWithLineBreaks = escapedHtmlAnswer.replace("\n", "<br>")
                    .replace(" ", "&nbsp;");
            this.setLocation(escapedHtmlAnswerWithLineBreaks);

            LOGGER.info("current location: " + currentLocation);
        } catch (JsonProcessingException e) {
            LOGGER.error("error in request in locationApi", e);
            throw new RuntimeException(e);
        }
    }

    public String getQUERY_NAME() {
        return QUERY_NAME;
    }

    public int getLIMIT() {
        return LIMIT;
    }

    public void setLocation(String location) {
        this.currentLocation = location;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }
}

