package com.khachungbg97gmail.carservice.Model;

/**
 * Created by ASUS on 10/9/2018.
 */

public class VideoYouTube {
    private String Title;
    private String Thumbnail;
    private String IdVideo;

    public VideoYouTube(String title, String thumbnail, String idVideo) {
        Title = title;
        Thumbnail = thumbnail;
        IdVideo = idVideo;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getIdVideo() {
        return IdVideo;
    }

    public void setIdVideo(String idVideo) {
        IdVideo = idVideo;
    }
}
