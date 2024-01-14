package at.qe.skeleton.internal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserReloadService {

    @Autowired
    private UserxDetailsService userxDetailsService;

    public void reloadUserAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Lassen Sie den Benutzer neu laden
        UserDetails updatedUserDetails = userxDetailsService.loadUserByUsername(auth.getName());

        // Erstellen Sie eine neue Authentifizierung mit aktualisierten Benutzerdetails
        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
                updatedUserDetails, auth.getCredentials(), updatedUserDetails.getAuthorities());

        // Setzen Sie die aktualisierte Authentifizierung zurück
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
    }
}

