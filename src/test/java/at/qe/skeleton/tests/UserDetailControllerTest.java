package at.qe.skeleton.tests;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.UserxService;
import at.qe.skeleton.internal.ui.controllers.UserDetailController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserDetailControllerTest {

    @Mock
    private UserxService userxService;
    @Spy
    @InjectMocks
    private UserDetailController userDetailController;
    @Before
    public void setUp() {MockitoAnnotations.initMocks(this);}

    @Test
    public void testDoRegister() throws IOException {
        Userx user = new Userx();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setEmail("testEmail");
        user.setPhone("testPhoneNumber");
        user.setFirstName("testFirstname");
        user.setLastName("testLastname");

        doNothing().when(userDetailController).redirectToLogin();
        when(userxService.loadUser("testUsername")).thenReturn(user);


        userDetailController.setUser(user);

        assertNotNull(userDetailController.getUser());

        Userx saved = userDetailController.doRegister();

        assertEquals("testUsername", user.getUsername());
        assertEquals("testEmail", user.getEmail());
        assertEquals("testPhoneNumber", user.getPhone());
        assertEquals("testFirstname", user.getFirstName());
        assertEquals("testLastname", user.getLastName());
        assertEquals(UserxRole.EMPLOYEE, user.getRoles().iterator().next());
    }

}
