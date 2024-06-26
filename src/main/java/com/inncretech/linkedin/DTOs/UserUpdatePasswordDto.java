package com.inncretech.linkedin.DTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePasswordDto {

    @NotBlank(message = "Current Password should not be blank")
    private String currentPassword;

    @NotBlank(message = "New Password is mandatory")
    @Size(min = 6, message = "password must have minimum of 6 characters")
    private String newPassword;
}
