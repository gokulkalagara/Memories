package com.maya.memories.models;

import java.io.Serializable;

/**
 * Created by home on 8/6/2017.
 */

public class VideoItem implements Serializable
{
    public String rollNumber,videoUrl,photoUrl;

    public VideoItem(String rollNumber, String videoUrl,String photoUrl) {
        this.rollNumber = rollNumber;
        this.videoUrl = videoUrl;
        this.photoUrl=photoUrl;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
