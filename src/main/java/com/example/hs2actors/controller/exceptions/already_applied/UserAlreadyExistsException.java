package com.example.hs2actors.controller.exceptions.already_applied;

public class UserAlreadyExistsException extends AlreadyAppliedException {
    public UserAlreadyExistsException(String login) {
        super("User with login = '" + login + "' already exists.");
    }
}
