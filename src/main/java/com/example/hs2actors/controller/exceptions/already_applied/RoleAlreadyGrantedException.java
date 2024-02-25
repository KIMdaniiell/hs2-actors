package com.example.hs2actors.controller.exceptions.already_applied;

import com.example.hs2actors.model.entity.Role;

public class RoleAlreadyGrantedException extends AlreadyAppliedException {
    public RoleAlreadyGrantedException(long id, Role role) {
        super("User (id = " + id + ") already is " + role.name());
    }
}
