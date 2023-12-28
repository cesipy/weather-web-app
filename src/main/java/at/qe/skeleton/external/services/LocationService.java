package at.qe.skeleton.external.services;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.repositories.LocationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Scope("application")
public class LocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private LocationRepository locationRepository;

    private final String PATH_NAME = "src/main/resources/owm_city_list.json";

    public List<Location> autocomplete(String name){
        return locationRepository.findByNameStartingWithIgnoreCase(name);
    }

    /**
     * Loads location data from .json file with path `path` and populates repository.
     * @param path path of .json file
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

    public void init() {
        LOGGER.info("starting population of database");
        loadDataFromJson(PATH_NAME);
        LOGGER.info("populated database");
    }
}
