package com.example.hs2actors.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.example.hs2actors.util.ValidationMessages.*;


@Data
@AllArgsConstructor
public class TeamManagerDTO {

    @Min(value = 0, message = MSG_ID_NEGATIVE)
    private Long id;

    @Min(value = 0, message = MSG_ID_NEGATIVE)
    @NotNull(message = "userId field can't be null")
    private Long userId;

    @NotNull(message = "firstName field can't be null")
    @NotBlank(message = "firstName field can't be blank")
    private String firstName;

    @NotNull(message = "lastName field can't be null")
    @NotBlank(message = "lastName field can't be blank")
    private String lastName;

    @Pattern(regexp = "^\\+[0-9]{11}$", message = "invalid format for phone number. See: '+12345678901'")
    private String phone;

    @NotNull(message = "email field can't be null")
    @NotBlank(message = "email field can't be blank")
    @Email
    private String email;
}
