package at.qe.skeleton.tests;

import at.qe.skeleton.internal.model.CreditCard;
import at.qe.skeleton.internal.model.Invoice;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.services.CreditCardService;
import at.qe.skeleton.internal.services.InvoiceService;
import at.qe.skeleton.internal.services.UserxService;
import at.qe.skeleton.internal.ui.beans.CashUpBean;
import at.qe.skeleton.internal.ui.beans.SessionInfoBean;
import at.qe.skeleton.internal.ui.beans.SubscriptionBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@WebAppConfiguration
@SpringBootTest(classes = at.qe.skeleton.Main.class)
class CashUpBeanTest {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private UserxService userService;
    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private SubscriptionBean subscriptionBean;
    @Autowired
    SessionInfoBean sessionInfoBean;

    @Autowired
    private CashUpBean cashUpBean;

    @WithMockUser(username= "user2")
    @Test
    public void offSetTest(){
        Userx testUser = userService.loadUser("user2");
        CreditCard testCreditCard = new CreditCard();
        Invoice testInvoice = new Invoice();
        testUser.setInvoice(testInvoice);

        testCreditCard.setBalance(0);
        testCreditCard.setCreditCard("123456");

        testUser.setCreditCard(testCreditCard);

        cashUpBean.offSet(testUser);

        assertFalse(sessionInfoBean.hasRole("PREMIUM"));
    }

    @WithMockUser(username= "user2", authorities = {"PREMIUM"})
    @Test
    public void offSetTestSufficientBalance(){
        Userx testUser = userService.loadUser("user2");
        CreditCard testCreditCard = new CreditCard();
        Invoice testInvoice = new Invoice();
        testUser.setInvoice(testInvoice);

        testCreditCard.setBalance(10);
        testCreditCard.setCreditCard("123456");

        testUser.setCreditCard(testCreditCard);

        cashUpBean.offSet(testUser);

        assertTrue(sessionInfoBean.hasRole("PREMIUM"));
    }

    @Test
    @WithMockUser(username= "user1", authorities = {"EMPOYEE"})
    void testGenerateInvoice() {
        Userx user = userService.loadUser("user1");
        cashUpBean.generateInvoice(user);
        assertNotNull(user.getInvoice());
    }

    @Test
    public  void testCalculatePrice(){
        Invoice testinvoice2 = new Invoice();
        assertEquals(1.0, cashUpBean.calculatePrice(testinvoice2), 0.1);
    }

}
