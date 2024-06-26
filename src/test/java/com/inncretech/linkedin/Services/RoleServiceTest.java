package com.inncretech.linkedin.Services;
import com.inncretech.linkedin.Models.*;
import com.inncretech.linkedin.DTOs.*;
import com.inncretech.linkedin.Repository.*;
import com.inncretech.linkedin.Mappers.*;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;


    private Roles roles;
    private RoleDto roleDto;

    private Long roleId;
    private String roleName;

    @BeforeMethod
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        roles = new Roles();
        roleId = 1L;
        roleName = "Role";
        roles.setId(roleId);
        roles.setRoleName(roleName);

        roleDto = new RoleDto();
        roleDto.setName(roleName);
    }

    @Test
    public void testGetAllRolesAllValid()
    {
        Roles role1 = new Roles();
        role1.setDeleted(false);
        Roles role2 = new Roles();
        role2.setDeleted(false);

        List<Roles> rolesList = Arrays.asList(role1,role2);

        RoleDto dto1 = new RoleDto();
        RoleDto dto2 = new RoleDto();

        when(roleRepository.findAll()).thenReturn(rolesList);
        when(roleMapper.roleToRoleDto(role1)).thenReturn(dto1);
        when(roleMapper.roleToRoleDto(role2)).thenReturn(dto2);

        List<RoleDto> result = roleService.getAllRoles();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains(dto1));
        Assert.assertTrue(result.contains(dto2));
    }

    @Test
    public void testGetAllRolesDeletedRoles()
    {
        Roles role1 = new Roles();
        role1.setDeleted(true);
        Roles role2 = new Roles();
        role2.setDeleted(true);

        List<Roles> rolesList = Arrays.asList(role1,role2);
        when(roleRepository.findAll()).thenReturn(rolesList);

        List<RoleDto> result = roleService.getAllRoles();

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllRolesEmptyList()
    {
        List<Roles> rolesList = new ArrayList<>();
        when(roleRepository.findAll()).thenReturn(rolesList);

        List<RoleDto> result = roleService.getAllRoles();
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRoleByIdValidId()
    {
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roles));
        when(roleMapper.roleToRoleDto(roles)).thenReturn(roleDto);

        RoleDto result = roleService.getRoleById(roleId);

        Assert.assertNotNull(result);
        Assert.assertEquals(result,roleDto);
    }

    @Test
    public void testGetRoleByIdInvalidId()
    {
        Roles obj = new Roles();
        obj.setRoleName("test role");
        obj.setId(9999L);

        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            RoleDto result = roleService.getRoleById(obj.getId());
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Role Not Found");
        }
    }

    @Test
    public void testGetRoleByIdDeletedRoleId()
    {
        roles.setDeleted(true);
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roles));
        try {
            RoleDto result = roleService.getRoleById(roleId);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Role Not Found");
        }
    }

    @Test
    public void testCreateRoleValidData()
    {

        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.empty());
        when(roleMapper.roleDtoToRole(roleDto)).thenReturn(roles);
        when(roleRepository.save(roles)).thenReturn(roles);
        when(roleMapper.roleToRoleDto(roles)).thenReturn(roleDto);

        RoleDto result = roleService.createRole(roleDto);

        Assert.assertNotNull(result);
        Assert.assertEquals(result,roleDto);
    }

    @Test
    public void testCreateRoleExistingRole()
    {
        Roles obj = new Roles();
        obj.setRoleName(roleName);

        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.of(roles));

        try{
            roleService.createRole(roleDto);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Role Already Exists!");
        }
    }
}
