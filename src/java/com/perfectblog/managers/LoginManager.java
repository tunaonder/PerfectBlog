/*
 * Created by Sait Tuna Onder on 2018.02.27  * 
 * Copyright © 2018 Sait Tuna Onder. All rights reserved. * 
 */
package com.perfectblog.managers;

import com.perfectblog.entityclasses.User;
import com.perfectblog.sessionbeans.UserFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Onder
 */
@Named(value = "loginManager")
@SessionScoped

public class LoginManager implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private String username;
    private String password;
    private String errorMessage;

    @EJB
    private UserFacade userFacade;

    // Constructor method instantiating an instance of LoginManager
    public LoginManager() {
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public String loginUser() {

        // Obtain the object reference of the User object from the entered username
        User user = getUserFacade().findByUsername(getUsername());

        if (user == null) {
            errorMessage = "Entered username " + getUsername() + " does not exist!";
            return "";

        } else {
            String actualUsername = user.getUsername();
            String enteredUsername = getUsername();

            String actualPassword = user.getPassword();
            String enteredPassword = getPassword();

            if (!actualUsername.equals(enteredUsername)) {
                errorMessage = "Invalid Username!";
                return "";
            }

            if (!actualPassword.equals(enteredPassword)) {
                errorMessage = "Invalid Password!";
                return "";
            }

            errorMessage = "";

            // Initialize the session map with user properties of interest
            initializeSessionMap(user);

            return "index.xhtml?faces-redirect=true";
        }
    }

    /*
    Initialize the session map with the user properties of interest,
    namely, first_name, last_name, username, and user_id.
    user_id = primary key of the user entity in the database
     */
    public void initializeSessionMap(User user) {
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("first_name", user.getFirstName());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("last_name", user.getLastName());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("username", username);
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user_id", user.getId());
    }

}