package com.maya.memories.models;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by home on 7/30/2017.
 */

public class ChatBoxItem implements Serializable {
    public String u_uid,userName,profilePic,message;
    public int readType;
    public Timestamp timestamp;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public ChatBoxItem(String u_uid, String userName, String profilePic, String message, int readType, Timestamp timestamp) {
        this.u_uid = u_uid;
        this.userName = userName;
        this.profilePic = profilePic;
        this.message = message;
        this.readType = readType;
        this.timestamp=timestamp;

    }

    public String getU_uid() {
        return u_uid;
    }

    public void setU_uid(String u_uid) {
        this.u_uid = u_uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getReadType() {
        return readType;
    }

    public void setReadType(int readType) {
        this.readType = readType;
    }
}
