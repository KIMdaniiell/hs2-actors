package com.example.hs2actors.controller.exceptions.fallback;

import com.example.hs2actors.controller.exceptions.ControllerException;

public class DubControllerException extends ControllerException {

    private final int statusCode;

    public DubControllerException(int statusCode, String error, String message) {
        super(error, message);
        this.statusCode = statusCode;
    }
}
