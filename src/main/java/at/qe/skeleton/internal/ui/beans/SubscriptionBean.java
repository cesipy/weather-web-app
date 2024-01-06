package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.UserxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class SubscriptionBean implements Serializable {

    @Autowired
    private UserxService userxService;
    public boolean isPremium() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals(UserxRole.PREMIUM.name()));
        }

        return false;
    }

    public boolean hasCreditCard(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null){
            Userx user = userxService.loadUser(authentication.getName());
            if(user.getCreditCard() != null){
                return true;
            }
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
        }
    }

}


