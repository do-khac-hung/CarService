package com.khachungbg97gmail.carservice.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ASUS on 10/10/2018.
 */

public class ServiceAddress {
    private LatLng loca;
    private String formatted_address;
    private String icon;
    private String id;
    private String name;

    public ServiceAddress(LatLng loca, String formatted_address, String icon, String id, String name) {
        this.loca = loca;
        this.formatted_address = formatted_address;
        this.icon = icon;
        this.id = id;
        this.name = name;
    }

    public LatLng getLoca() {
        return loca;
    }

    public void setLoca(LatLng loca) {
        this.loca = loca;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
