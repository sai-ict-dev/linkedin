package com.inncretech.linkedin.Services;
import com.inncretech.linkedin.Models.*;
import com.inncretech.linkedin.DTOs.*;
import com.inncretech.linkedin.Repository.*;



import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class CompanyFollowerServiceTest {

    @Mock
    private CompanyFollowerRepository companyFollowerRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private CompanyFollowerService companyFollowerService;

    private Long companyId;
    private Long userId;
    private Long roleId;
    private String email;
    private Company company;
    private User user;
    private Roles roles;

    private Company_followers companyFollowers;
    private CompanyFollowersDto companyFollowersDto;


    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        company = new Company();
        companyId = 1L;
        company.setId(companyId);
        company.setName("random company");
        company.setProfile("randome profile");

        userId = 1L;
        email = "testinn@gmail.com";
        user = new User();
        user.setId(userId);
        user.setFirstName("test first name");
        user.setLastName("test Last Name");
        user.setMobile(7862848232L);
        user.setEmail(email);
        user.setPassword("test@456");

        roles = new Roles();
        roleId = 1L;
        roles.setId(roleId);
        roles.setRoleName("randome role");

        companyFollowers = new Company_followers();
        companyFollowers.setId(1L);
        companyFollowers.setCompany(company);
        companyFollowers.setUser(user);
        companyFollowers.setRole(roles);

        companyFollowersDto = new CompanyFollowersDto();
        companyFollowersDto.setCompanyId(companyId);
        companyFollowersDto.setFollowerId(userId);
        companyFollowersDto.setRoleId(roleId);
    }

    @Test
    public void testGetAllCompanyFollowers() {
        Company company1 = new Company();
        Company company2 = new Company();

        company1.setId(3L);
        company2.setId(4L);


        Company_followers follower1 = new Company_followers();
        follower1.setCompany(company1);

        Company_followers follower2 = new Company_followers();
        follower2.setCompany(company2);

        Company_followers followers3 = new Company_followers();
        followers3.setCompany(company1);

        Company_followers followers4 = new Company_followers();
        followers4.setCompany(company1);

        when(companyFollowerRepository.findByCompanyId(company1.getId())).thenReturn(Arrays.asList(follower1, followers3,followers4));

        List<Company_followers> result = companyFollowerService.getAllCompanyFollowers(company1.getId());

        assertNotNull(result);
        assertEquals(result.size(), 3);
    }

    @Test
    public void testGetAllCompanyFollowersInvalidCompanyId()
    {
        Long id = 999L;
        List<Company_followers> list = new ArrayList<>();
        when(companyFollowerRepository.findByCompanyId(id)).thenReturn(list);

        List<Company_followers> result = companyFollowerService.getAllCompanyFollowers(id);

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCreateCompanyFollower_Success() {

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roles));

        companyFollowerService.createCompanyFollower(companyFollowersDto);

        verify(companyRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).findById(1L);
        verify(companyFollowerRepository, times(1)).save(any(Company_followers.class));
    }

    @Test
    public void testCreateCompanyFollowerCompanyNotFound() {
        Long CompanyId = 99L;
        companyFollowersDto.setCompanyId(companyId);

        when(companyRepository.findById(CompanyId)).thenReturn(Optional.empty());
        try {
            companyFollowerService.createCompanyFollower(companyFollowersDto);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Company Not Found with the given Id");
        }
    }

    @Test
    public void testCreateCompanyFollowerDeletedCompany()
    {
        company.setDeleted(true);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        try {
            companyFollowerService.createCompanyFollower(companyFollowersDto);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Company Not Found with the given Id");
        }
    }



    @Test
    public void testCreateCompanyFollowerFollowerNotFound() {
        Long userId = 99L;
        companyFollowersDto.setFollowerId(userId);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        try {
            companyFollowerService.createCompanyFollower(companyFollowersDto);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Follower Not Found with the given Id");
        }
    }

    @Test
    public void testCreateCompanyFollowerDeletedUser()
    {
        user.setDeleted(true);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        try {
            companyFollowerService.createCompanyFollower(companyFollowersDto);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Follower Not Found with the given Id");
        }
    }

    @Test
    public void testCreateCompanyFollowerRoleNotFound() {
        Long roleId = 99L;
        companyFollowersDto.setRoleId(roleId);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
        try {
            companyFollowerService.createCompanyFollower(companyFollowersDto);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"role Not Found with the given Id");
        }
    }

    @Test
    public void testCreateCompanyFollowerDeletedRole()
    {
        roles.setDeleted(true);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roles));
        try {
            companyFollowerService.createCompanyFollower(companyFollowersDto);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"role Not Found with the given Id");
        }
    }

}
