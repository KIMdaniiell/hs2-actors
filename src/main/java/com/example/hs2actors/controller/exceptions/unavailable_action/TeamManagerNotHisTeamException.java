package com.example.hs2actors.controller.exceptions.unavailable_action;


public class TeamManagerNotHisTeamException extends UnavailableActionException {
    public TeamManagerNotHisTeamException(long teamManagerId, long teamId) {
        super("Team manager (id = " + teamManagerId
                + ") can't do such actions with team (id = " + teamId + "), it is not his team.");
    }
}
