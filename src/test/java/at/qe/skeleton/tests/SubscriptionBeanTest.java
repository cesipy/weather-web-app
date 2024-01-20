package at.qe.skeleton.tests;

import at.qe.skeleton.internal.model.CreditCard;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.UserReloadService;
import at.qe.skeleton.internal.services.UserxService;
import at.qe.skeleton.internal.ui.beans.CashUpBean;
import at.qe.skeleton.internal.ui.beans.SessionInfoBean;
import at.qe.skeleton.internal.ui.beans.SubscriptionBean;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionBeanTest {

    class TestableSubscriptionBean extends SubscriptionBean {

        @Mock
        private SecurityContext securityContext;

        @Override
        protected SecurityContext getSecurityContext() {
            return securityContext;
        }
    }

    @Mock
    private UserxService userxService;

    @Mock
    private UserReloadService userReloadService;

    @Mock
    private CashUpBean cashUpBean;

    @Mock
    private SessionInfoBean sessionInfoBean;

    @InjectMocks
    private SubscriptionBean subscriptionBean;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**@Test
    void testIsPremium() {
        when(sessionInfoBean.hasRole("PREMIUM")).thenReturn(true);
        subscriptionBean.init();

        assertTrue(subscriptionBean.isPremium());
    }**/

    @Test
    void testHasCreditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.setCreditCard("123456789");
        creditCard.setBalance(1000);

        // Mocking
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        Userx userWithCreditCard = new Userx();
        userWithCreditCard.setCreditCard(creditCard);

        when(userxService.loadUser(anyString())).thenReturn(userWithCreditCard);
        TestableSubscriptionBean testableSubscriptionBean = new TestableSubscriptionBean();

        assertTrue(testableSubscriptionBean.hasCreditCard());
    }

    /**@Test
    void testToggleSubscription() {
        // Mocking
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        Userx user = new Userx();
        user.setUsername("testUser");

        when(userxService.loadUser(anyString())).thenReturn(user);

        // Test premium to non-premium transition
        when(subscriptionBean.isPremium()).thenReturn(true);
        when(subscriptionBean.hasCreditCard()).thenReturn(true);
        subscriptionBean.toggleSubscription();

        verify(userxService).removeUserRole("testUser", UserxRole.PREMIUM);
        verify(cashUpBean).offSet(user);

        // Test non-premium to premium transition
        when(subscriptionBean.isPremium()).thenReturn(false);
        subscriptionBean.toggleSubscription();

        verify(userxService).addUserRole("testUser", UserxRole.PREMIUM);
        verify(cashUpBean).generateInvoice(user);
    }

    @Test
    void testToggleSubscriptionValidationException() {
        // Mocking
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Test validation exception when no credit card
        when(subscriptionBean.isPremium()).thenReturn(true);
        when(subscriptionBean.hasCreditCard()).thenReturn(false);

        subscriptionBean.toggleSubscription();

        // Verify that UserRole is not removed and offSet is not called
        verify(userxService, never()).removeUserRole(anyString(), any());
        verify(cashUpBean, never()).offSet(any());

        // Verify that FacesContext message is added
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide payment information", null);
        verify(FacesContext.getCurrentInstance()).addMessage(null, facesMessage);
    }**/
}

