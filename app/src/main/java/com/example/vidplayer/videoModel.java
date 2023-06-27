package com.example.vidplayer;

import android.graphics.Bitmap;

public class videoModel {
    private String videoName;
    private String videoPath;
    private Bitmap thumb;

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public videoModel(String videoName, String videoPath, Bitmap thumb) {
        this.videoName = videoName;
        this.videoPath = videoPath;
        this.thumb = thumb;

    }
}
