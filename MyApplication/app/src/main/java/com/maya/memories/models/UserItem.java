package com.maya.memories.models;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by home on 8/2/2017.
 */

public class UserItem implements Serializable
{
    public String u_uid,firstName,lastName,email,login_id,profile_pic;

    public String getU_uid() {
        return u_uid;
    }

    public void setU_uid(String u_uid) {
        this.u_uid = u_uid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public UserItem(String u_uid, String firstName, String lastName, String email, String login_id, String profile_pic) {
        this.u_uid = u_uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.login_id = login_id;
        this.profile_pic = profile_pic;
    }



}
