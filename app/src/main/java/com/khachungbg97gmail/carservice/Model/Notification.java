package com.khachungbg97gmail.carservice.Model;

/**
 * Created by ASUS on 10/17/2018.
 */

public class Notification {
    public  String title;
    public  String body;
    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
