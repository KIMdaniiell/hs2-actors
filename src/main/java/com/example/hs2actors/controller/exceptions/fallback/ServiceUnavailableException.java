package com.example.hs2actors.controller.exceptions.fallback;


import com.example.hs2actors.controller.exceptions.ControllerException;

public class ServiceUnavailableException extends ControllerException {

    public ServiceUnavailableException(String message) {
        super("Service unavailable", message);
    }
}
