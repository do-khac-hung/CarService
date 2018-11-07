package com.khachungbg97gmail.carservice.Model;

/**
 * Created by ASUS on 11/6/2018.
 */

public class DecodeValue {
    private String label;
    private String value;

    public DecodeValue(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
