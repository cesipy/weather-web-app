package at.qe.skeleton.tests;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.UserxService;
import at.qe.skeleton.internal.ui.controllers.UserDetailController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.*;
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
    public void testDoRegister(){
        Userx user = new Userx();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setEmail("testEmail");
        user.setFirstName("testFirstname");
        user.setLastName("testLastname");

        doNothing().when(userDetailController).redirectToLogin();
        when(userxService.loadUser("testUsername")).thenReturn(user);
        when(userDetailController.doEncodePassword(user.getPassword())).thenReturn(user.getPassword());

        userDetailController.setUser(user);

        assertNotNull(userDetailController.getUser());

        Userx saved = userDetailController.doRegister();

        assertEquals("testUsername", saved.getUsername());
        assertEquals("testEmail", saved.getEmail());
        assertEquals("testPassword", saved.getPassword());
        assertEquals("testFirstname", saved.getFirstName());
        assertEquals("testLastname", saved.getLastName());
        assertEquals(UserxRole.EMPLOYEE, user.getRoles().iterator().next());
    }
}
