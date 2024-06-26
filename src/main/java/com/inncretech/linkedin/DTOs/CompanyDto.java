package com.inncretech.linkedin.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {

    @NotBlank(message = "Company name is mandatory")
    private String name;

    @NotBlank(message = "Profile is mandatory")
    private String profile;
}
