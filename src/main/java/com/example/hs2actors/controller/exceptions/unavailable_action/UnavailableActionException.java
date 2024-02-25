package com.example.hs2actors.controller.exceptions.unavailable_action;

import com.example.hs2actors.controller.exceptions.ControllerException;

public class UnavailableActionException extends ControllerException {
    public UnavailableActionException(String message) {
        super("Action is unavailable", message);
    }
}
