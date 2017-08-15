package com.maya.memories.models;

import java.sql.Timestamp;

/**
 * Created by home on 8/1/2017.
 */

public class ChatItem
{
    public String u_uid,message;
    public int messageType;
    public Timestamp timestamp;

    public ChatItem(String u_uid, String message, int messageType, Timestamp timestamp) {
        this.u_uid = u_uid;
        this.message = message;
        this.messageType = messageType;
        this.timestamp = timestamp;
    }

    public String getU_uid() {
        return u_uid;
    }

    public void setU_uid(String u_uid) {
        this.u_uid = u_uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
