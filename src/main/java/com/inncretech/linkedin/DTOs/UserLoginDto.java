package com.inncretech.linkedin.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    @NotBlank(message = "user name is mandatory")
    private String userName;

    @NotBlank(message = "password is mandatory")
    private String password;
}
