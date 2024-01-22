package at.qe.skeleton.tests;

import at.qe.skeleton.external.controllers.EmptyLocationException;
import at.qe.skeleton.external.controllers.FavoriteController;
import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.FavoriteService;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@WebAppConfiguration
public class FavoriteControllerIntegrationTest {

    @Autowired      // for some unknown reason i have to use MockBean instead of @Autowired...
    private FavoriteController favoriteController;

    @Mock
    private FacesContext facesContext;

/*
    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        // Mock FacesContext.getCurrentInstance() to return the mocked FacesContext
        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);
        favoriteController.init();
    }


    @Test
    @WithMockUser(username = "admin" , authorities = {"ADMIN"})
    public void testSaveFavorite() {
        String query = "Vienna";
        favoriteController.setLocationName(query);

        favoriteController.saveFavorite();

        favoriteController.retrieveFavorites();
        List<Favorite> favorites = favoriteController.getFavorites();
        System.out.println(favorites.size());

    }



 */
}
