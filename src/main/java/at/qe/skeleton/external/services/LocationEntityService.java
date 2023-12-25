package at.qe.skeleton.external.services;

import at.qe.skeleton.external.model.location.LocationEntity;
import at.qe.skeleton.external.repositories.LocationEntityRepository;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Scope("application")
public class LocationEntityService {

    @Autowired
    private LocationEntityRepository locationEntityRepository;

    public List<LocationEntity> autocomplete(String name){
        return locationEntityRepository.findByNameStartingWithIgnoreCase(name);
    }

    public void loadDataFromJson(String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(path);
            List<LocationEntity> locationEntityList = objectMapper.readValue(file, new TypeReference<List<LocationEntity>>() {});

            locationEntityRepository.saveAll(locationEntityList);
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
