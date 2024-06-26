package com.inncretech.linkedin.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostAddDto {
    @NotBlank(message = "Post content is mandatory")
    private String content;

    @NotNull(message = "user id should not be blank")
    private Long userId;

    @NotNull(message = "company id is mandatory")
    private Long companyId;
}
