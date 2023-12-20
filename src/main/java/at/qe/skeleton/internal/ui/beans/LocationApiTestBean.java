package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.location.LocationListDTO;
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

@Component
@Scope("view")
public class LocationApiTestBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationApiTestBean.class);

    @Autowired
    private LocationApiRequestService locationApiRequestService;

    private String location;
    private String name = "Innsbruck";


    @PostConstruct
    public void init() {
        try {
            LocationListDTO answer = this.locationApiRequestService.retrieveLocations(getName());
            ObjectMapper mapper = new ObjectMapper()
                    .findAndRegisterModules()
                    .enable(SerializationFeature.INDENT_OUTPUT);

            String plainTextAnswer = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(answer);
            String escapedHtmlAnswer = StringEscapeUtils.escapeHtml4(plainTextAnswer);
            String escapedHtmlAnswerWithLineBreaks = escapedHtmlAnswer.replace("\n", "<br>")
                    .replace(" ", "&nbsp;");
            this.setLocation(escapedHtmlAnswerWithLineBreaks);

            LOGGER.info("current location: " + location);
        } catch (JsonProcessingException e) {
            LOGGER.error("error in request in locationApi", e);
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

