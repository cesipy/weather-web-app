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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserxDetailsService implements UserDetailsService {

    @Autowired
    private UserxRepository userxRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Benutzer aus der Datenbank laden
        Userx user = userxRepository.findFirstByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Benutzer nicht gefunden: " + username);
        }

        // Rückgabe von UserDetails-Objekt
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), getAuthorities(user.getRoles()));
    }

    // Hilfsmethode, um Benutzerrollen in SimpleGrantedAuthority-Objekte umzuwandeln
    private Collection<? extends GrantedAuthority> getAuthorities(Set<UserxRole> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }
}

