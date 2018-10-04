package com.khachungbg97gmail.carservice.Model;

/**
 * Created by ASUS on 10/4/2018.
 */

public class User {
    private  String phone;
    private  String name;
    private  String password;
    private  String email;
    private  String firstName;
    private  String lastName;

    public User() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String Pphone) {
        phone = Pphone;
    }

    public String getName() {
        return name;
    }

    public void setName(String pname) {
        name = pname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String ppassword) {
        password = ppassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String pemail) {
        email = pemail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String pfirstName) {
        firstName = pfirstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String plastName) {
        lastName = plastName;
    }

    public User(String pphone, String pname, String ppassword, String pemail, String pfirstName, String plastName) {

        phone = pphone;
        name = pname;
        password = ppassword;
        email = pemail;
        firstName = pfirstName;
        lastName = plastName;
    }
}
