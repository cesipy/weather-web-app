package at.qe.skeleton.configs;

import at.qe.skeleton.external.services.LocationService;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup {

    private final LocationService locationService;

    public ApplicationStartup(LocationService locationEntityService) {
        this.locationService = locationEntityService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeAtStartup() {
        locationService.init();
    }
}
