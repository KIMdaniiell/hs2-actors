package com.example.hs2actors.controller.exceptions.unavailable_action;


public class TeamNoSpaceException extends UnavailableActionException {
    public TeamNoSpaceException(long teamId) {
        super("Can't add player to team (id = " + teamId + "), it has no space.");
    }
}
