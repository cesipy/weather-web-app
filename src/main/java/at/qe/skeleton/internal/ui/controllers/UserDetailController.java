package at.qe.skeleton.internal.ui.controllers;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.UserxService;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the user detail view.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("view")
public class UserDetailController implements Serializable {

    @Autowired
    private UserxService userService;

    /**
     * Attribute to cache the currently displayed user
     */
    private Userx user = new Userx();

    /**
     * Sets the currently displayed user and reloads it form db. This user is
     * targeted by any further calls of
     * {@link #doReloadUser()}, {@link #doSaveUser()} and
     * {@link #doDeleteUser()}.
     *
     * @param user
     */
    public void setUser(Userx user) {
        this.user = user;
        doReloadUser();
    }

    /**
     * Returns the currently displayed user.
     *
     * @return
     */
    public Userx getUser() {
        return user;
    }

    /**
     * Action to force a reload of the currently displayed user.
     */
    public void doReloadUser() {
        user = userService.loadUser(user.getUsername());
    }

    /**
     * Action to save the currently displayed user.
     */
    public void doSaveUser() {user = this.userService.saveUser(user);}

    /**
     * Action to register the currently displayed user
     */

    public void doRegister() throws IOException {
        user.setCreateUser(user);
        user.setEnabled(true);
        Set<UserxRole> roles = new HashSet<>();
        roles.add(UserxRole.EMPLOYEE);
        user.setRoles(roles);
        user = this.userService.saveUser(user);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/login.xhtml");
        } catch (ValidationException ve) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ve.getMessage(), null));
        }
    }

    /**
     * Action to delete the currently displayed user.
     */
    public void doDeleteUser() {
        this.userService.deleteUser(user);
        user = null;
    }
}
