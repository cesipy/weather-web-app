package at.qe.skeleton.external.services;

import at.qe.skeleton.external.controllers.EmptyLocationException;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.location.LocationDTO;
import at.qe.skeleton.external.repositories.LocationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.time.Duration;
import java.time.Instant;


/**
 * Service class for managing locations retrieved from the database.
 */
@Service
@Scope("application")
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationApiRequestService locationApiRequestService;

    // points to "src/main/resources/owm_city_list.json"
    // json file with cities is from: https://github.com/manifestinteractive/openweathermap-cities
    @Value("${location.data.file.path:src/main/resources/owm_city_list.json}")
    private String filePath;


    /**
     * Provides a list of locations based on the given query for autocomplete functionality.
     *
     * @param name query for autocompletion.
     * @return list of locations matching the provided query.
     */
    public List<Location> autocomplete(String name) throws EmptyLocationException {
        if (name != null && !name.trim().isEmpty()) {
            return locationRepository.findByNameStartingWithIgnoreCase(name);
        }
        String message = String.format("The given location: %s is empty!", name);
        throw new EmptyLocationException(message);
    }

    public Location retrieveLocationByExactName(String name) {
        return locationRepository.findFirstByName(name);
    }


    public Location retrieveLocation(String name) throws EmptyLocationException, ApiQueryException {
        Location location =  locationRepository.findFirstByNameStartingWithIgnoreCase(name);

        // if location is also null in the api call, EmptyLocationException is thrown
        if (location == null) {
            List<LocationDTO> locationDTOS =  locationApiRequestService.retrieveLocations(name, 1);
            return locationApiRequestService.convertLocationDTOtoLocation(locationDTOS.get(0));
        }
        return location;
    }

    public List<Location> getAllLocations(){
        return locationRepository.findAll();
    }
    /**
     * Loads location data from .json file with path `path` and populates repository.
     *
     * @param path path of .json file.
     */
    public void loadDataFromJson(String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(path);
            List<Location> locationEntityList = objectMapper.readValue(file, new TypeReference<List<Location>>() {});

            locationRepository.saveAll(locationEntityList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Initializes the service by populating the database with location data.
     */
    public void init() {
        logger.info("Starting population of database");
        Instant start = Instant.now();
        loadDataFromJson(filePath);
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);


        logger.info("Populated database in {}s", duration.toSeconds());
    }

    public String getFilePath() {
        return filePath;
    }

}
