package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.location.LocationEntity;
import at.qe.skeleton.external.services.LocationEntityService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
@Scope("view")
public class LocationAutocompleteDemoBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationAutocompleteDemoBean.class);

    @Autowired
    private LocationEntityService locationEntityService;

    private final String PATH_NAME = "src/main/resources/owm_city_list.json";

    public String locationName;

    List<LocationEntity> locations;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void demo() {
        if (locationName == null) {
            LOGGER.info("query is null!");
        }
        locations = locationEntityService.autocomplete(locationName);
        LOGGER.info("successfully autocomplete");

        for (LocationEntity location : locations) {
            LOGGER.info(String.valueOf(location));
        }
    }
}
