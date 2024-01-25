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

@Component
public class SubscriptionBean implements Serializable {

    @Autowired
    private transient UserxService userxService;
    @Autowired
    private transient UserReloadService userReloadService;
    @Autowired
    private transient CashUpBean cashUpBean;

    private String buttonText;

    @PostConstruct
    public void init() {
        updateButtonText();

    }

    public String getButtonText() {
        return buttonText;
    }

    public boolean isPremium() {
        SessionInfoBean sessionInfoBean = new SessionInfoBean();
        return sessionInfoBean.hasRole("PREMIUM");
    }

    public boolean hasCreditCard() {
        Authentication authentication = getSecurityContext().getAuthentication();

        Userx user = userxService.loadUser(authentication.getName());
        return user.getCreditCard() != null;

        return false;
    }

    protected SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

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


    private void updateButtonText() {
        buttonText = isPremium() ? "Unsubscribe" : "Subscribe" ;
    }
}
