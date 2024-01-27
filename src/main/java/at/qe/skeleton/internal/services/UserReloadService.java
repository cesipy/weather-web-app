package at.qe.skeleton.internal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;

/**
 * Service class for reloading user authentication.
 * This service is responsible for reloading the authentication of the current user,
 * ensuring that the user's details are up-to-date in the security context.
 *
 * @see Service
 */
@Service
public class UserReloadService {

    @Autowired
    private UserxDetailsService userxDetailsService;

    /**
     * Reloads the authentication of the current user.
     * This method loads the updated user details, creates a new authentication token
     * with the updated details, and sets the updated authentication in the security context.
     */
    public void reloadUserAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Load the updated user details
        UserDetails updatedUserDetails = userxDetailsService.loadUserByUsername(auth.getName());

        // Create a new authentication with updated user details
        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
                updatedUserDetails, auth.getCredentials(), updatedUserDetails.getAuthorities());

        // Set the updated authentication back
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
    }
}
