package com.cursoandroid.consultamoedas.model;

public class Moeda {

    String code, name, bid;

    public Moeda() {
    }

    public Moeda(String code, String name, String bid) {
        this.code = code;
        this.name = name;
        this.bid = bid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    @Override
    public String toString(){
        return code + " - " + name;
    }
}