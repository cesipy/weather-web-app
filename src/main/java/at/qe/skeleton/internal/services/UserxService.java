package at.qe.skeleton.internal.services;

import at.qe.skeleton.external.model.WeatherDataField;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.repositories.UserxRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @PreAuthorize("hasAuthority('MANAGER')")
    public Collection<Userx> findPremiumUser() {
        return userRepository.findByRole(UserxRole.PREMIUM);
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
            user.setSelectedFields(List.of(WeatherDataField.TEMP, WeatherDataField.FEELS_LIKE, WeatherDataField.DESCRIPTION));
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

    /**
     * Retrieves the currently authenticated user.
     * This method uses Spring Security's `SecurityContextHolder` to obtain
     * the authentication information .
     *
     * @return The currently authenticated user.
     */
    public Userx getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

    /**
     * Removes a specified role from the roles of a user.
     *
     * @param username   The username of the user from whom the role should be removed.
     * @param userxRole  The role to be removed from the user's roles.
     */
    public void removeUserRole(String username, UserxRole userxRole) {
        Userx userx = loadUser(username);
        Set<UserxRole> oldRoles = userx.getRoles();

        if (oldRoles.remove(userxRole)) {
            userx.setRoles(oldRoles);
            saveUser(userx);
        }
    }

    /**
     * Adds a specified role to the roles of a user.
     *
     * @param username   The username of the user to whom the role should be added.
     * @param userxRole  The role to be added to the user's roles.
     */
    public void addUserRole(String username, UserxRole userxRole) {
        Userx userx = loadUser(username);
        Set<UserxRole> oldRoles = userx.getRoles();
        userx.setPremiumSince(LocalDateTime.now());

        if (oldRoles.add(userxRole)) {
            userx.setRoles(oldRoles);
            saveUser(userx);
        }
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * This method uses the Spring Security context to obtain information about
     * the currently authenticated user and returns the corresponding Userx entity.
     *
     * @return The Userx entity representing the currently authenticated user.
     */
    public Userx getCurrentUser() {
        return getAuthenticatedUser();
    }

    /**
     * Retrieves the selected weather data fields for the currently authenticated user for the overview page.
     *
     * This method fetches the currently authenticated user, then retrieves and returns
     * the list of selected weather data fields associated with that user.
     *
     * @return A list of WeatherDataFields that represent the selected fields for the overview page.
     */
    public List<WeatherDataField> getSelectedWeatherFieldsForUser() {
        Userx userx = getCurrentUser();

        return userx.getSelectedFields();
    }

    /**
     * Adds new selected weather data fields to the currently authenticated user's selected fields.
     *
     * This method fetches the currently authenticated user, appends the new selected weather data fields
     * to the existing list, and updates the user's preferences.
     *
     * @param newSelectedFields A list of WeatherDataFields to be added to the user's selected fields.
     */
    public void addSelectedWeatherFieldsForUser(List<WeatherDataField> newSelectedFields) {
        Userx userx = getCurrentUser();
        List<WeatherDataField> selectedFields = userx.getSelectedFields();

        selectedFields.addAll(newSelectedFields);

        userx.setSelectedFields(selectedFields);
        userRepository.save(userx);
    }


    /**
     * Removes selected weather data fields from the currently authenticated user's selected fields.
     *
     * This method fetches the currently authenticated user, removes selected weather data fields
     * and updates the user's preference.
     *
     * @param toDeleteSelectedFields A list of WeatherDataFields to be added to the user's selected fields.
     */
    public void deleteSelectedWeatherFieldsForUser(List<WeatherDataField> toDeleteSelectedFields) {
        Userx userx = getCurrentUser();
        List<WeatherDataField> selectedFields = userx.getSelectedFields();

        selectedFields.removeAll(toDeleteSelectedFields);
        userRepository.save(userx);

    }

}
