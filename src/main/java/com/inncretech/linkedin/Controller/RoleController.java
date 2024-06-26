package com.inncretech.linkedin.Controller;

import com.inncretech.linkedin.DTOs.RoleDto;
import com.inncretech.linkedin.Services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/Role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/allRoles")
    public ResponseEntity<List<RoleDto>> getAllRoles()
    {
        List<RoleDto> roleDtoList = roleService.getAllRoles();
        return new ResponseEntity<>(roleDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(Long id)
    {
        RoleDto roleDto = roleService.getRoleById(id);
        return new ResponseEntity<>(roleDto,HttpStatus.OK);
    }

    @PostMapping("/createRole")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto)
    {
        RoleDto createdRole = roleService.createRole(roleDto);
        return new ResponseEntity<>(createdRole,HttpStatus.CREATED);
    }
}
