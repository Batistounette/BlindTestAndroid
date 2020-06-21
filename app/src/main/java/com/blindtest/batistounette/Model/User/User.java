package com.blindtest.batistounette.Model.User;

/**
 * Created by Batistounette on 15/05/2020.
 */

public class User {

    private int mID;
    private String mFullname, mUsername, mEmail, mPassword;

    public User(int id, String fullname, String username, String email, String password) {
        this.setID(id);
        this.setFullname(fullname);
        this.setUsername(username);
        this.setEmail(email);
        this.setPassword(password);
    }

    int getID() {
        return mID;
    }

    void setID(int id) {
        mID = id;
    }

    String getFullname() {
        return mFullname;
    }

    void setFullname(String fullname) {
        mFullname = fullname;
    }

    String getUsername() {
        return mUsername;
    }

    void setUsername(String username) {
        mUsername = username;
    }

    String getEmail() {
        return mEmail;
    }

    void setEmail(String email) {
        mEmail = email;
    }

    String getPassword() {
        return mPassword;
    }

    void setPassword(String password) {
        mPassword = password;
    }
}