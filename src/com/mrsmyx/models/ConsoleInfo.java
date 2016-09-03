package com.mrsmyx.models;

import com.mrsmyx.CCAPI;

/**
 * Created by cj on 1/5/16.
 */
public class ConsoleInfo {


    private String firmware;
    private CCAPI.ConsoleType type;

    private ConsoleInfo(){}

    public static ConsoleInfo Builder(){
        return new ConsoleInfo();
    }

    public String getFirmware() {
        return firmware;
    }

    public ConsoleInfo setFirmware(String firmware) {
        this.firmware = "FW: " + firmware;
        return this;
    }

    /**
     * Returns the console type
     * @return Console Type, DEX, CEX, TOOL
     */
    public CCAPI.ConsoleType getType() {
        return type;
    }

    public ConsoleInfo setType(CCAPI.ConsoleType type) {
        this.type = type;
        return this;
    }

    public String getCellC() {
        return  "CELL: " + temperature.getCellC();
    }

    public String getRsxC() {
        return "RSX: " + temperature.getRsxC();
    }

    public String getCellF() {
        return  "CELL: " + temperature.getCellF();
    }

    public String getRsxF() {
        return "RSX: " + temperature.getRsxF();
    }

    /**
     * Gets the temperature of the ps3
     * @return CELL & RSX Temperature
     * @see Temperature
     */
    public Temperature getTemperature(){
        return temperature;
    }

    public ConsoleInfo setTemperature(Temperature temperature){
        this.temperature = temperature;
        return this;
    }
    private Temperature temperature;
}
