package com.inncretech.linkedin.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleDto {

    @NotBlank(message = "Role name is mandatory")
    private String name;
}
