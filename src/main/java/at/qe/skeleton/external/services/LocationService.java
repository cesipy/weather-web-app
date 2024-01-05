package at.qe.skeleton.external.services;

import at.qe.skeleton.external.controllers.EmptyLocationException;
import at.qe.skeleton.external.model.location.Location;
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
import java.util.Objects;


/**
 * Service class for managing locations retrieved from the database.
 */
@Service
@Scope("application")
public class LocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private LocationRepository locationRepository;

    // points to "src/main/resources/owm_city_list.json"
    // json file with cities is from: https://github.com/manifestinteractive/openweathermap-cities
    @Value("${location.data.file.path:src/main/resources/owm_city_list.json}")
    private String filePath;


    /**
     * Provides a list of locations based on the given query for autocomplete functionality.
     *
     * @param name query for autocompletion.
     * @return list of locations matching the provided query.
     * @throws EmptyLocationException If the provided name is empty.
     */
    public List<Location> autocomplete(String name) throws EmptyLocationException {
        if (Objects.equals(name, "")) {
            LOGGER.info("throwing exception in LocationService");
            throw new EmptyLocationException();
        }
        else {
            return locationRepository.findByNameStartingWithIgnoreCase(name);
        }

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
        LOGGER.info("starting population of database");
        loadDataFromJson(filePath);
        LOGGER.info("populated database");
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public String getFilePath() {
        return filePath;
    }

}
