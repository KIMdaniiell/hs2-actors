package com.example.hs2actors.model.dto;

import com.example.hs2actors.model.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    @NotNull(message = "role field can't be null")
    private Role role;
}
