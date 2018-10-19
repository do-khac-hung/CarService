package com.khachungbg97gmail.carservice.Model;

/**
 * Created by ASUS on 10/19/2018.
 */

public class Vin {
    private  String vinCode;
    private  String idUser;
    private  String category;
    private  boolean type; //kiểm tra triệu hồi

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public Vin() {
    }

    public Vin(String vinCode, String idUser, String category) {
        this.vinCode = vinCode;
        this.idUser = idUser;
        this.category = category;
        this.type=true;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
