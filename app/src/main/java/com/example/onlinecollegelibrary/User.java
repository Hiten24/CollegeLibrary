package com.example.onlinecollegelibrary;

public class User {
    private String name,email,sapId,studentClass,div,address,mobNo,pass;
    public User(){

    }

    public User(String name,String sapId){
        this.name = name;
        this.sapId = sapId;
    }

    public User(String name, String email, String sapId, String studentClass, String div, String address, String mobNo, String pass) {
        this.name = name;
        this.email = email;
        this.sapId = sapId;
        this.studentClass = studentClass;
        this.div = div;
        this.address = address;
        this.mobNo = mobNo;
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSapId() {
        return sapId;
    }

    public void setSapId(String sapId) {
        this.sapId = sapId;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
