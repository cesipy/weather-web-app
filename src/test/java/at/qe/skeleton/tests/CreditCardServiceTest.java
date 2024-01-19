package at.qe.skeleton.tests;

import at.qe.skeleton.internal.model.CreditCard;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.CreditCardService;
import at.qe.skeleton.internal.services.UserxService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Set;

@SpringBootTest
@WebAppConfiguration
public class CreditCardServiceTest {

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private UserxService userxService;

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    public void testCreditCardService() {
        // Erstellen Sie einen Benutzer für die CreditCard
        Userx user = new Userx();
        user.setUsername("user");
        user.setPassword("password");
        user.setEmail("user@example.com");
        user.setLastName("Doe");
        user.setFirstName("John");

        Set<UserxRole> set = Set.of(UserxRole.ADMIN);
        user.setRoles(set);
        userxService.saveUser(user);

        // Erstellen Sie eine CreditCard für den Benutzer
        CreditCard creditCard = new CreditCard();
        creditCard.setCreditCard("1234567890123456");
        creditCard.setBalance(1000.0);
        creditCard.setUserx(user);

        // Testen Sie die Speicherfunktion
        CreditCard savedCreditCard = creditCardService.saveCreditCard(creditCard);
        Assertions.assertNotNull(savedCreditCard);
        Assertions.assertEquals("1234567890123456", savedCreditCard.getCreditCard());

        // Testen Sie die Aktualisierung des Guthabens
        double price = 500.0;
        creditCardService.updateBalance(price, savedCreditCard);
        CreditCard updatedCreditCard = creditCardService.getCreditCardById(savedCreditCard.getCreditCard());
        Assertions.assertNotNull(updatedCreditCard);
        Assertions.assertEquals(500.0, updatedCreditCard.getBalance());

        // Testen Sie die Löschfunktion
        //String creditCardId = updatedCreditCard.getCreditCard();
        //creditCardService.deleteCreditCard(updatedCreditCard);
        //CreditCard deletedCreditCard = creditCardService.getCreditCardById(creditCardId);
        //Assertions.assertNull(deletedCreditCard);
    }
}
