package com.maya.memories.models;

import java.io.Serializable;

/**
 * Created by Administrator on 8/1/2017.
 */

public class ClassItem implements Serializable {
   public String profilePic,rollNo;
   int numPhotos,numVideos;

    public ClassItem(String rollNo, String profilePic, int numPhotos, int numVideos) {
        this.rollNo = rollNo;
        this.profilePic = profilePic;
        this.numPhotos = numPhotos;
        this.numVideos = numVideos;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public int getNumPhotos() {
        return numPhotos;
    }

    public void setNumPhotos(int numPhotos) {
        this.numPhotos = numPhotos;
    }

    public int getNumVideos() {
        return numVideos;
    }

    public void setNumVideos(int numVideos) {
        this.numVideos = numVideos;
    }
}
