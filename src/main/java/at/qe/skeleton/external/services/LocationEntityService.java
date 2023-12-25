package at.qe.skeleton.external.services;

import at.qe.skeleton.external.model.location.LocationEntity;
import at.qe.skeleton.external.repositories.LocationEntityRepository;
import at.qe.skeleton.internal.ui.beans.LocationAutocompleteDemoBean;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Scope("application")
public class LocationEntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationEntityService.class);

    @Autowired
    private LocationEntityRepository locationEntityRepository;

    private final String PATH_NAME = "src/main/resources/owm_city_list.json";

    public List<LocationEntity> autocomplete(String name){
        return locationEntityRepository.findByNameStartingWithIgnoreCase(name);
    }

    /**
     * Loads location data from .json file with path `path` and populates repository.
     * @param path path of .json file
     */
    public void loadDataFromJson(String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(path);
            List<LocationEntity> locationEntityList = objectMapper.readValue(file, new TypeReference<List<LocationEntity>>() {});

            locationEntityRepository.saveAll(locationEntityList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void init() {
        LOGGER.info("starting population of database");
        loadDataFromJson(PATH_NAME);
        LOGGER.info("populated database");
    }
}
