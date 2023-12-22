package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.location.LocationEntity;
import at.qe.skeleton.external.repositories.LocationEntityRepository;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class LocationEntityController {

    @Autowired
    private LocationEntityRepository locationEntityRepository;

    @PostConstruct
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            InputStream inputStream = getClass().getResourceAsStream("currentcitylist.json");
            List<LocationEntity> locationEntities = objectMapper.readValue(inputStream, new TypeReference<List<LocationEntity>>() {});
            locationEntityRepository.saveAll(locationEntities);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
