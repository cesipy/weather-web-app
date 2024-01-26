package at.qe.skeleton.internal.services;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.repositories.UserxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for loading user details during authentication.
 * This service implements the UserDetailsService interface and is responsible
 * for loading user details from the database for authentication purposes.
 *
 * @see Service
 * @see UserDetailsService
 */
@Service
public class UserxDetailsService implements UserDetailsService {

    @Autowired
    private UserxRepository userxRepository;

    /**
     * Loads user details by the specified username.
     *
     * @param username The username of the user to load.
     * @return The UserDetails object for the specified user.
     * @throws UsernameNotFoundException If the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load user from the database
        Userx user = userxRepository.findFirstByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // Return UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), getAuthorities(user.getRoles()));
    }

    /**
     * Converts user roles to a collection of SimpleGrantedAuthority objects.
     *
     * @param roles The set of roles assigned to the user.
     * @return A collection of SimpleGrantedAuthority objects representing the user roles.
     */
    private Collection<? extends GrantedAuthority> getAuthorities(Set<UserxRole> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }
}
