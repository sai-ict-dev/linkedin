package com.inncretech.linkedin.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyFollowersDto {

    @NotNull(message = "company Id is mandatory")
    private Long companyId;

    @NotNull(message = "Follower Id is mandatory")
    private Long followerId;

    @NotNull(message = "Ro Id is mandatory")
    private Long roleId;
}
