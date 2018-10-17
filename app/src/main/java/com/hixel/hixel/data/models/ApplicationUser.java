package com.hixel.hixel.data.models;

/**
 * Temporary model that sends data to the server, note that this should not be used with Room
 * as we don't want to store passwords.
 */
public class ApplicationUser {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    /**
     *  Constructs a (partial) ApplicationUser to be sent to the server during registration.
     * All other usages of the object are fully populated and received from the server.
     *
     * @param firstName The FirstName of the User.
     * @param lastName The last name of the user.
     * @param email The email of the user.
     * @param password The password of the user.
     */
    public ApplicationUser(String firstName, String lastName,  String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}