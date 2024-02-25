package com.example.hs2actors.controller.exceptions.already_applied;


import com.example.hs2actors.controller.exceptions.ControllerException;

public class AlreadyAppliedException extends ControllerException {
    public AlreadyAppliedException(String message) {
        super("Already applied action", message);
    }
}
