package com.inncretech.linkedin.Services;

import com.inncretech.linkedin.DTOs.RoleDto;
import com.inncretech.linkedin.Mappers.RoleMapper;
import com.inncretech.linkedin.Models.Roles;
import com.inncretech.linkedin.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    public List<RoleDto> getAllRoles()
    {
        List<Roles> roles = roleRepository.findAll();
        return roles.stream().filter(role -> !role.isDeleted())
                .map(roleMapper::roleToRoleDto)
                .collect(Collectors.toList());
    }

    public RoleDto getRoleById(Long id)
    {
        Optional<Roles> roleFromDB = roleRepository.findById(id);
        if(roleFromDB.isPresent() && !roleFromDB.get().isDeleted())
        {
            Roles role = roleFromDB.get();
            return roleMapper.roleToRoleDto(role);
        }
        else{
            throw new IllegalArgumentException("Role Not Found");
        }
    }

    public RoleDto createRole(RoleDto roleDto)
    {

        Optional<Roles> roleFromDB = roleRepository.findByRoleName(roleDto.getName());
        if(roleFromDB.isPresent() && !roleFromDB.get().isDeleted())
        {
            throw new IllegalArgumentException("Role Already Exists!");
        }

        Roles role = roleMapper.roleDtoToRole(roleDto);

        Roles savedRole = roleRepository.save(role);
        return roleMapper.roleToRoleDto(savedRole);
    }


}
