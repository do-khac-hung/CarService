package com.khachungbg97gmail.carservice.Model;

/**
 * Created by ASUS on 10/21/2018.
 */

public class Schedule {
    private String date;
    private String note;
    private String idUser;
    private String idVin;
    private String accessory;
    private String time;

    public String getTime() {
        return time;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public Schedule() {
    }

    public Schedule(String date, String note, String idUser, String idVin, String accessory, String time) {
        this.date = date;
        this.note = note;
        this.idUser = idUser;
        this.idVin = idVin;
        this.accessory = accessory;
        this.time = time;
    }

    public Schedule(String date, String note, String idUser, String accessory) {
        this.date = date;
        this.note = note;
        this.idUser = idUser;
        this.accessory = accessory;
    }

    public Schedule(String date, String note, String idUser, String idVin, String accessory) {
        this.date = date;
        this.note = note;
        this.idUser = idUser;
        this.idVin = idVin;
        this.accessory = accessory;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdVin() {
        return idVin;
    }

    public void setIdVin(String idVin) {
        this.idVin = idVin;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }
}
