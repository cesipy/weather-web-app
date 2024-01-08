package at.qe.skeleton.internal.services;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.repositories.UserxRepository;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

/**
 * Service for accessing and manipulating user data.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("application")
public class UserxService {

    private static Logger LOGGER = LoggerFactory.getLogger(UserxService.class);
    @Autowired
    private UserxRepository userRepository;

    /**
     * Returns a collection of all users.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Userx> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public Userx loadUser(String username) {
        return userRepository.findFirstByUsername(username);
    }

    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public Userx loadUserContaining(String username) {
        return userRepository.findFirstByUsernameContaining(username);
    }

    /**
     * Saves the user. This method will also set {@link Userx#createDate} for new
     * entities or {@link Userx#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link Userx#createDate}
     * or {@link Userx#updateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     */


    public Userx saveUser(Userx user) {
        if (user.isNew()) {
            user.setCreateUser(getAuthenticatedUser());
        } else {
            user.setUpdateUser(getAuthenticatedUser());
        }
        return userRepository.save(user);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(Userx user) {
        userRepository.delete(user);
    }

    public Userx getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }
    
    public void removeUserRole(String username, UserxRole userxRole) {
        Userx userx = loadUser(username);
        Set<UserxRole> oldRoles = userx.getRoles();

        if (oldRoles.remove(userxRole)) {
            userx.setRoles(oldRoles);
            saveUser(userx);
        }

    }

    public void addUserRole(String username, UserxRole userxRole) {
        Userx userx = loadUser(username);
        Set<UserxRole> oldRoles = userx.getRoles();

        if (oldRoles.add(userxRole)) {
            userx.setRoles(oldRoles);
            saveUser(userx);
        }

    }

    public Userx getCurrentUser() {
        Userx authenticatedUser = getAuthenticatedUser();

        return authenticatedUser;
    }



}
