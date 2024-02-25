package com.example.hs2actors.controller.exceptions.unavailable_action;


public class TeamClosedException extends UnavailableActionException {
    public TeamClosedException(long teamId) {
        super("Can't add player to team (id = " + teamId + "), it is closed -- only it's team manager can add players.");
    }
}
