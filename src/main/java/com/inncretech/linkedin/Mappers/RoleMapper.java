package com.inncretech.linkedin.Mappers;

import com.inncretech.linkedin.DTOs.RoleDto;
import com.inncretech.linkedin.Models.Roles;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RoleMapper {

    public RoleDto roleToRoleDto(Roles roles){
        RoleDto roleDto = new RoleDto();
        roleDto.setName(roles.getRoleName());

        return roleDto;
    }

    public Roles roleDtoToRole(RoleDto roleDto)
    {
        Roles role = new Roles();
        role.setRoleName(roleDto.getName());
        role.setDeleted(false);
        role.setUpdatedAt(LocalDateTime.now());
        role.setUpdatedAt(LocalDateTime.now());

        return role;
    }
}
