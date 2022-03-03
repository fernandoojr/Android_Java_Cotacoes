package com.cursoandroid.consultamoedas.model;

public class Moeda {

    String code, name;

    public Moeda() {
    }

    public Moeda(String code, String name) {
        this.code = code;
        this.name = name;
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

    @Override
    public String toString(){
        return code + " - " + name;
    }
}