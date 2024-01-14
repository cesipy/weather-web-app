package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.UserReloadService;
import at.qe.skeleton.internal.services.UserxService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

@Component
public class SubscriptionBean implements Serializable {

    @Autowired
    private UserxService userxService;

    @Autowired
    private UserReloadService userReloadService;

    private String buttonText;

    @PostConstruct
    public void init() {
        updateButtonText();
    }

    public String getButtonText() {
        return buttonText;
    }

    /**public boolean isPremium() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // Überprüfe, ob die Rolle "PREMIUM" in den Authorities vorhanden ist
            return authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals(UserxRole.PREMIUM.name()));
        }

        return false;
    }**/

    public boolean isPremium() {
        SessionInfoBean sessionInfoBean = new SessionInfoBean();
        return sessionInfoBean.hasRole("PREMIUM");
    }

    public boolean hasCreditCard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Userx user = userxService.loadUser(authentication.getName());
            return user.getCreditCard() != null;
        }

        return false;
    }

    public void toggleSubscription() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            String username = authentication.getName();

            // Überprüfe, ob der Benutzer die Rolle "PREMIUM" hat
            if (isPremium()) {
                // Der Benutzer hat die Rolle "PREMIUM", entferne sie
                userxService.removeUserRole(username, UserxRole.PREMIUM);
            } else {
                // Der Benutzer hat die Rolle "PREMIUM" nicht, füge sie hinzu
                userxService.addUserRole(username, UserxRole.PREMIUM);
            }

            userReloadService.reloadUserAuthentication();


            /** neues login
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            try {
                externalContext.redirect(externalContext.getRequestContextPath() + "/login.xhtml");
            } catch (ValidationException | IOException ve) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ve.getMessage(), null));
            }**/

            // Aktualisiere den Button-Text nach dem Umschalten
            updateButtonText();

        }
    }

    private void updateButtonText() {
        buttonText = isPremium() ? "Deabonnieren" : (hasCreditCard() ? "Abonnieren" : "Bitte Zahlungsinformation hinterlegen");
    }
}
