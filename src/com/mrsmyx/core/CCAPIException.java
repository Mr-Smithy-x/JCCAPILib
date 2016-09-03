package com.mrsmyx.core;

/**
 * Created by cj on 1/5/16.
 */
public class CCAPIException extends Throwable {

    private CCAPIERROR error;
    private String errorMsg;

    public CCAPIException(CCAPIERROR error, String errorMsg) {
        this.error = error;
        this.errorMsg = errorMsg;
    }

    @Override
    public String getMessage() {
        return  String.format("%s\n%s", getLocalizedMessage(),getStackTrace().toString());
    }

    @Override
    public String getLocalizedMessage() {
        return  String.format("%s: %s", error.name(),errorMsg);
    }

    @Override
    public String toString() {
        return getLocalizedMessage();
    }
}
