package at.qe.skeleton.internal.ui.controllers;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.services.UserxService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Controller for the user list view.
 * This class is responsible for managing user-related operations in the user list view.
 *
 * @see Component
 * @see Scope
 */
@Component
@Scope("view")
public class UserListController implements Serializable {

    @Autowired
    private transient UserxService userService;

    /**
     * Returns a collection of all users.
     *
     * @return The collection of all Userx entities.
     */
    public Collection<Userx> getUsers() {
        return userService.getAllUsers();
    }

    /**
     * Returns a list of premium users.
     *
     * @return The list of premium Userx entities.
     */
    public List<Userx> getPremiumUsers() {
        return new ArrayList<>(userService.findPremiumUser());
    }

    /**
     * Returns a list of all possible roles.
     *
     * @return The list of all possible roles as strings.
     */
    public List<String> getAllRoles() {
        return Arrays.asList("ADMIN", "MANAGER", "EMPLOYEE", "PREMIUM");
    }
}
