package com.hixel.hixel.service.models;

public class ApplicationUser {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private Portfolio portfolio;

    //Constructs a (partial) ApplicationUser to be sent to the server during registration
    //All other usages of the object are fully populated and received from the server.
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

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
}
