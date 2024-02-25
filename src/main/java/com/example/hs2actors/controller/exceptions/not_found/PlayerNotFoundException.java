package com.example.hs2actors.controller.exceptions.not_found;


public class PlayerNotFoundException extends NotFoundException {
    public PlayerNotFoundException(String filtersString) {
        super("player", filtersString);
    }
}
