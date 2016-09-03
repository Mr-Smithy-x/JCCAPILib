package com.mrsmyx.models;

/**
 * Created by cj on 1/6/16.
 */
public class Process {
    public String pid;
    public String pidName;

    private Process(){}

    public static Process Builder(){
        return new Process();
    }

    public String getPid() {
        return pid;
    }

    public Process setPid(String pid) {
        this.pid = pid;
        return this;
    }

    public String getPidName() {
        return pidName;
    }

    public Process setPidName(String pidName) {
        this.pidName = pidName;
        return this;
    }
}
