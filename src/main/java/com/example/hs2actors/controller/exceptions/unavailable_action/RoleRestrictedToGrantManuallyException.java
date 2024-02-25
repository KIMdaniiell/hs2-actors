package com.example.hs2actors.controller.exceptions.unavailable_action;

import com.example.hs2actors.model.entity.Role;

public class RoleRestrictedToGrantManuallyException extends UnavailableActionException {
    public RoleRestrictedToGrantManuallyException(Role role) {
        super("User need to create entity for getting role:" + role.name());
    }
}
