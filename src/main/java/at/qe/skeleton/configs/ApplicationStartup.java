package at.qe.skeleton.configs;

import at.qe.skeleton.external.services.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup {

    private static Logger LOGGER = LoggerFactory.getLogger(ApplicationStartup.class);

    private final LocationService locationService;

    public ApplicationStartup(LocationService locationEntityService) {
        this.locationService = locationEntityService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeAtStartup() {
        locationService.init();
    }
}
