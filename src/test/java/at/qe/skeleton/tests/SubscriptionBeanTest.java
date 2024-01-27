package at.qe.skeleton.tests;

import at.qe.skeleton.internal.ui.beans.SubscriptionBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@WebAppConfiguration
public class SubscriptionBeanTest {
    @Autowired
    SubscriptionBean subscriptionBean;
    @Test
    @WithMockUser(username = "user1", authorities = {"PREMIUM"})
    public void testIsPremium(){
        Assertions.assertTrue(subscriptionBean.isPremium());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"PREMIUM"})
    public void testHasCreditCard(){
        Assertions.assertFalse(subscriptionBean.hasCreditCard());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"PREMIUM"})
    public void testToggleSubscription(){
        assertThrows(NullPointerException.class, () -> {
            subscriptionBean.toggleSubscription();
        });
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"PREMIUM"})
    public void testUpdateButtonTextIsPremium(){
        subscriptionBean.updateButtonText();
        subscriptionBean.getButtonText();
        String buttonText = subscriptionBean.getButtonText();
        assertEquals("Unsubscribe", buttonText);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"MANAGER"})
    public void testUpdateButtonTextNotPremium(){
        subscriptionBean.updateButtonText();
        subscriptionBean.getButtonText();
        String buttonText = subscriptionBean.getButtonText();
        assertEquals("Subscribe", buttonText);
    }
}
