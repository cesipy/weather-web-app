package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.UserReloadService;
import at.qe.skeleton.internal.services.UserxService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;

import jakarta.faces.context.FacesContext;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Managed Bean for handling user subscription.
 * This bean is responsible for managing user subscriptions, toggling between
 * premium and non-premium status, checking credit card information, and updating
 * the user's subscription status.
 *
 * @see Component
 */
@Component
public class SubscriptionBean implements Serializable {

    @Autowired
    private UserxService userxService;
    @Autowired
    private UserReloadService userReloadService;
    @Autowired
    private CashUpBean cashUpBean;

    private String buttonText;

    /**
     * Initializes the bean by updating the button text.
     */
    @PostConstruct
    public void init() {
        updateButtonText();
    }

    /**
     * Gets the button text.
     *
     * @return The button text.
     */
    public String getButtonText() {
        return buttonText;
    }

    /**
     * Checks if the user has the PREMIUM role.
     *
     * @return True if the user has the PREMIUM role, false otherwise.
     */
    public boolean isPremium() {
        SessionInfoBean sessionInfoBean = new SessionInfoBean();
        return sessionInfoBean.hasRole("PREMIUM");
    }

    /**
     * Checks if the user has a credit card.
     *
     * @return True if the user has a credit card, false otherwise.
     */
    public boolean hasCreditCard() {
        Authentication authentication = getSecurityContext().getAuthentication();

        if (authentication != null) {
            Userx user = userxService.loadUser(authentication.getName());
            System.out.println("username:" + user.getUsername());
            System.out.println("credit card von user" + user.getCreditCard());
            return user.getCreditCard() != null;
        }

        System.out.println("authentication = 0");
        return false;
    }

    /**
     * Toggles the user's subscription between premium and non-premium status.
     */
    public void toggleSubscription() {
        Authentication authentication = getSecurityContext().getAuthentication();
        Userx user = userxService.loadUser(authentication.getName());

        try {
            if (!hasCreditCard()) {
                throw new ValidationException();
            }

            if (authentication != null) {
                String username = authentication.getName();

                if (isPremium()) {
                    userxService.removeUserRole(username, UserxRole.PREMIUM);
                    cashUpBean.offSet(user);
                } else {
                    userxService.addUserRole(username, UserxRole.PREMIUM);
                    cashUpBean.generateInvoice(user);
                }
                userReloadService.reloadUserAuthentication();
                updateButtonText();
            }
        } catch (ValidationException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide payment information", e.getMessage())
            );
        }
    }

    /**
     * Gets the security context.
     *
     * @return The security context.
     */
    protected SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    /**
     * Updates the button text based on the user's subscription status.
     */
    private void updateButtonText() {
        buttonText = isPremium() ? "Unsubscribe" : "Subscribe";
    }
}
