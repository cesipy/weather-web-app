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
public class LocationAutocompleteDemo {

    private static Logger LOGGER = LoggerFactory.getLogger(LocationAutocompleteDemo.class);

    @Autowired
    private LocationEntityService locationEntityService;

    private final String PATH_NAME = "src/main/resources/owm_city_list.json";

    List<LocationEntity> locations;

    @PostConstruct
    public void init() {
        locationEntityService.loadDataFromJson(PATH_NAME);
    }

    public void demo() {
        locations = locationEntityService.autocomplete("innsb");
        LOGGER.info("successfully autocomplete");

        for (LocationEntity location : locations) {
            LOGGER.info(String.valueOf(location));
        }
    }
}
