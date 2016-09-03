package com.mrsmyx.models;


/**
 * Created by cj on 1/6/16.
 */
public class Temperature {

    public String cell, rsx;

    private Temperature() {}

    private Temperature(String cell, String rsx){
        this.cell = cell;
        this.rsx = rsx;
    }

    public static Temperature Builder(String cell, String rsx) {
        return new Temperature(cell,rsx);
    }



    public String getCellC() {
        return cell;
    }

    public String getCellF(){
        return String.valueOf(Integer.valueOf(cell) * 9/5 + 32);
    }

    public String getRsxF(){
        return String.valueOf(Integer.valueOf(rsx) * 9/5 + 32);
    }

    public String getRsxC() {
        return rsx;
    }

}