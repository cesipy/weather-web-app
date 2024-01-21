package at.qe.skeleton.internal.ui.controllers;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.EmailService;
import at.qe.skeleton.internal.services.UserxDetailsService;
import at.qe.skeleton.internal.services.UserxService;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserxDetailsService userxDetailsService;
    public BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String tempPassword;

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewRole() {
        return newRole;
    }

    public void setNewRole(String newRole) {
        this.newRole = newRole;
    }

    private String newUsername;
    private String newRole;

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
    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
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

    public Userx doRegister(){
        try {
            this.userService.saveUser(user);

            Set<UserxRole> roles = new HashSet<>();
            roles.add(UserxRole.EMPLOYEE);
            user.setRoles(roles);
            user.setPassword(doEncodePassword(user.getPassword()));
            user.setEnabled(true);
            user.setCreateUser(user);

            this.userService.saveUser(user);
            Userx saved = this.userService.loadUser(user.getUsername());

            redirectToLogin();
            return saved;
        } catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
            return null;
        }
    }


    /**
     * Action to delete the currently displayed user.
     */

    public void resetOldPassword(){
        if(user.getUsername()!=null){
            tempPassword = generatePassword();
            Userx user2 = userService.passwordService(user.getUsername(), doEncodePassword(tempPassword));

            String sendTo = user2.getEmail();
            String subject = "Reset password";
            String body ="Redirect to this Page and Enter the new Passwort to reset the old one: http://localhost:8080/resetPassword.xhtml\n Your Temporary Passwort is: "+tempPassword;
            emailService.sendEmail(sendTo, subject, body);
        }
    }

   public String generatePassword() {
        int passwordLength = 8;
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < passwordLength; i++) {
            int digit = random.nextInt(10);
            password.append(digit);
        }
        return password.toString();
   }

   public void saveResetPasswort(){
        if(passwordEncoder.matches(tempPassword, userxDetailsService.loadUserByUsername(user.getUsername()).getPassword())){
            userService.passwordService(user.getUsername(), doEncodePassword(user.getPassword()));
            redirectToLogin();
        }else{
            //real Exception Handling is missing
            System.out.println("Wrong Password");
        }
   }

    public void redirectToLogin(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/login.xhtml");
        } catch (ValidationException | IOException ve) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ve.getMessage(), null));
        }
    }

    public String doEncodePassword(String password){return passwordEncoder.encode(password);}

    /**
     * Action to delete the currently displayed user.
     */
    public void doDeleteUser() {
        this.userService.deleteUser(user);
        user = null;
    }

    public void doAddUserRole(){
        userService.addUserRole(newUsername, UserxRole.valueOf(newRole));
    }

    public void doRemoveUserRole(){
        userService.removeUserRole(newUsername, UserxRole.valueOf(newRole));
    }
}
