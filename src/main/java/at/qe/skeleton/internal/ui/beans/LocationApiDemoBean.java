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
public class LocationApiDemoBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationApiDemoBean.class);

    @Autowired
    private LocationApiRequestService locationApiRequestService;

    private String currentLocation;
    private String query_name;
    private final int LIMIT = 1;


    @PostConstruct
    public void init() {
        try {
            List<LocationDTO> answer = this.locationApiRequestService.retrieveLocations(getQuery_name(), getLIMIT());

            // Check if the list is not empty
            if (!answer.isEmpty()) {
                // only process first entry in List of LocationDTOs
                LocationDTO firstLocation = answer.get(0);

                //LOGGER.info("location name: "+firstLocation.name());
                //LOGGER.info("location lat&lon: " + firstLocation.latitude() + ", " + firstLocation.longitude());

                ObjectMapper mapper = new ObjectMapper()
                        .findAndRegisterModules()
                        .enable(SerializationFeature.INDENT_OUTPUT);

                String plainTextAnswer = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(firstLocation);
                String escapedHtmlAnswer = StringEscapeUtils.escapeHtml4(plainTextAnswer);
                String escapedHtmlAnswerWithLineBreaks = escapedHtmlAnswer.replace("\n", "<br>")
                        .replace(" ", "&nbsp;");
                this.setLocation(escapedHtmlAnswerWithLineBreaks);

                LOGGER.info("current location: " + currentLocation);
            } else {
                LOGGER.warn("The list of locations is empty.");
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("error in request in locationApi", e);
            throw new RuntimeException(e);
        }
    }


    public String getQuery_name() {
        return query_name;
    }

    public void setQuery_name(String query_name) {
        this.query_name = query_name;
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

