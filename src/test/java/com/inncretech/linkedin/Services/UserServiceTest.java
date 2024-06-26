package com.inncretech.linkedin.Services;

import com.inncretech.linkedin.Models.*;
import com.inncretech.linkedin.DTOs.*;
import com.inncretech.linkedin.Repository.*;
import com.inncretech.linkedin.Mappers.*;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.OpenOption;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private Long userId;
    private String email;

    private User user;

    private UserRegistrationDto userRegistrationDto;
    private UserLoginDto userLoginDto;
    private UserUpdateDto updateDto;
    private UserUpdatePasswordDto updatePasswordDto;

    @BeforeMethod
    public void SetUp(){
        MockitoAnnotations.openMocks(this);

        userId = 1L;
        email = "testinn@gmail.com";
        user = new User();
        user.setId(userId);
        user.setFirstName("test first name");
        user.setLastName("test Last Name");
        user.setMobile(7862848232L);
        user.setEmail(email);
        user.setPassword("test@456");

        userLoginDto = new UserLoginDto();
        userLoginDto.setUserName(email);
        userLoginDto.setPassword("test@456");


    }

    @Test(dependsOnMethods = "InitiliazeUserRegistrationDto")
    public void testCreateUserValidUser()
    {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userMapper.userRegistrationDtoToUser(userRegistrationDto)).thenReturn(user);
        when(passwordEncoder.encode(userRegistrationDto.getPassword())).thenReturn("test@456");
        when(userMapper.userToUserRegistrationDto(user)).thenReturn(userRegistrationDto);
        when(userRepository.save(user)).thenReturn(user);

        UserRegistrationDto result = userService.createUser(userRegistrationDto);

        Assert.assertNotNull(result);
        Assert.assertEquals(result,userRegistrationDto);
    }

    @Test
    public void InitiliazeUserRegistrationDto()
    {
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setFirstName("test first name");
        userRegistrationDto.setLastName("test Last Name");
        userRegistrationDto.setMobile(7862848232L);
        userRegistrationDto.setEmail("test@gmail.com");
        userRegistrationDto.setPassword("test@456");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreateUserExistingUser()
    {
        User obj = new User();
        obj.setEmail(email);
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(obj));

        userService.createUser(dto);
    }

    @Test(dependsOnMethods = "InitiliazeUserRegistrationDto")
    public void testCreateUserDeletedUser()
    {
        user.setDeleted(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.userRegistrationDtoToUser(userRegistrationDto)).thenReturn(user);
        when(passwordEncoder.encode(userRegistrationDto.getPassword())).thenReturn("test@456");
        when(userMapper.userToUserRegistrationDto(user)).thenReturn(userRegistrationDto);
        when(userRepository.save(user)).thenReturn(user);

        UserRegistrationDto result = userService.createUser(userRegistrationDto);

        Assert.assertNotNull(result);
        Assert.assertEquals(result,userRegistrationDto);
    }

    @Test
    public void testgetAllUsersNonDeletedUsers()
    {
        User user1 = new User();
        user1.setDeleted(false);

        User user2 = new User();
        user2.setDeleted(false);

        User user3 = new User();
        user3.setDeleted(false);

        List<User> userList = Arrays.asList(user1,user2,user3);

        UserRegistrationDto dto1 = new UserRegistrationDto();
        UserRegistrationDto dto2 = new UserRegistrationDto();
        UserRegistrationDto dto3 = new UserRegistrationDto();


        when(userRepository.findAll()).thenReturn(userList);

        when(userMapper.userToUserRegistrationDto(user1)).thenReturn(dto1);
        when(userMapper.userToUserRegistrationDto(user2)).thenReturn(dto2);
        when(userMapper.userToUserRegistrationDto(user3)).thenReturn(dto3);


       List<UserRegistrationDto> result =  userService.getAllUsers();
       Assert.assertNotNull(result);
       Assert.assertTrue(result.contains(dto1));
        Assert.assertTrue(result.contains(dto2));
        Assert.assertTrue(result.contains(dto3));
    }


    @Test
    public void testgetAllUsersDeletedUsers()
    {
        User user1 = new User();
        user1.setDeleted(true);

        User user2 = new User();
        user2.setDeleted(false);

        User user3 = new User();
        user3.setDeleted(false);

        List<User> userList = Arrays.asList(user2,user3);

        UserRegistrationDto dto2 = new UserRegistrationDto();
        UserRegistrationDto dto3 = new UserRegistrationDto();


        when(userRepository.findAll()).thenReturn(userList);

        when(userMapper.userToUserRegistrationDto(user2)).thenReturn(dto2);
        when(userMapper.userToUserRegistrationDto(user3)).thenReturn(dto3);


        List<UserRegistrationDto> result =  userService.getAllUsers();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains(dto2));
        Assert.assertTrue(result.contains(dto3));
    }

    @Test
    public void testGetAllUsersEmptyDB(){

        List<User> userList = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(userList);

        List<UserRegistrationDto> result = userService.getAllUsers();

        Assert.assertTrue(result.isEmpty());
    }

    @Test(dataProvider = "userLoginDtos",dependsOnMethods = "InitiliazeUserRegistrationDto")
    public void testValidateUserValidLogin(UserLoginDto userLoginDto)
    {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userLoginDto.getPassword(),user.getPassword())).thenReturn(true);
        when(userMapper.userToUserRegistrationDto(user)).thenReturn(userRegistrationDto);

        UserRegistrationDto result = userService.validateUser(userLoginDto);

        Assert.assertNotNull(result);
        Assert.assertEquals(result,userRegistrationDto);
    }

    @DataProvider(name = "userLoginDtos")
    public Object[] userLoginDto()
    {
        return new Object[]{userLoginDto};
    }

    @DataProvider(name = "wrongLoginPasswordDto")
    public Object[] LoginDtoWrongPassword()
    {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setUserName(email);
        loginDto.setPassword("wrong password");
        return new Object[]{loginDto};
    }

    @Test(dataProvider = "wrongLoginPasswordDto", dependsOnMethods = "InitiliazeUserRegistrationDto")
    public void testValidateUserWrongPassword(UserLoginDto userLoginDto){
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userLoginDto.getPassword(),userRegistrationDto.getPassword())).thenReturn(false);

        try{
            UserRegistrationDto registrationDto = userService.validateUser(userLoginDto);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"UserName or password incorrect. Please check and try again");
        }
    }

    @Test
    public void testValidateUserInvalidUser()
    {

        UserLoginDto test = new UserLoginDto();
        test.setUserName("inn@gm.com");
        when(userRepository.findByEmail(test.getUserName())).thenReturn(Optional.empty());
        try{
            UserRegistrationDto result = userService.validateUser(test);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"User does not found with the given email");
        }
    }

    @Test
    public void testValidateUserDeletedUser()
    {
        user.setDeleted(true);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        try{
            UserRegistrationDto result = userService.validateUser(userLoginDto);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"User does not found with the given email");
        }
    }

    @Test(dependsOnMethods = "InitiliazeUserRegistrationDto")
    public void testGetUserValidEmail()
    {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.userToUserRegistrationDto(user)).thenReturn(userRegistrationDto);

        UserRegistrationDto result = userService.getUser(email);

        Assert.assertNotNull(result);
        Assert.assertEquals(result,userRegistrationDto);
    }

    @Test
    public void testGetUserInvalidEmail()
    {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        try {
            UserRegistrationDto result = userService.getUser("bvsbfibsfls@gm.com");

        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"User Not Found with the mail " + "bvsbfibsfls@gm.com");
        }
    }

    @Test
    public void testGetUserDeletedEmail()
    {
        user.setDeleted(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        try{
            UserRegistrationDto result = userService.getUser(email);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"User Not Found with the mail " + email);
        }
    }

    @DataProvider(name = "validUpdateDto")
    public Object[] validUpdatedto()
    {
        updateDto = new UserUpdateDto();
        updateDto.setFirstName("updated first name");
        updateDto.setLastName("updated Last name");
        updateDto.setMobile(9999999999L);
        return new Object[]{updateDto};
    }

    @Test(dataProvider = "validUpdateDto",dependsOnMethods = "InitiliazeUserRegistrationDto")
    public void testUpdatePersonalDetailsValidEmail(UserUpdateDto updateDto)
    {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserRegistrationDto(user)).thenReturn(userRegistrationDto);

        UserRegistrationDto result = userService.updatePersonalDetails(email,updateDto);

        Assert.assertNotNull(result);
        Assert.assertEquals(user.getFirstName(),updateDto.getFirstName());
        Assert.assertEquals(user.getLastName(),updateDto.getLastName());
        Assert.assertEquals(user.getMobile(),updateDto.getMobile());
    }

    @Test(dataProvider = "validUpdateDto",dependsOnMethods = "InitiliazeUserRegistrationDto")
    public void testUpdatePersonalDetailsInvalideEmail(UserUpdateDto updateDto)
    {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        try{
            userService.updatePersonalDetails("invalidgmail@gmail.com", updateDto);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"User not found with email " + "invalidgmail@gmail.com");
        }
    }

    @Test(dataProvider = "validUpdateDto",dependsOnMethods = "InitiliazeUserRegistrationDto")
    public void testUpdatePersonalDetailsDeletedMail(UserUpdateDto updateDto)
    {
        user.setDeleted(true);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        try{
            userService.updatePersonalDetails(email,updateDto);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"User not found with email " + email);
        }
    }

    @Test
    public void testDeleteUserValidEmail()
    {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.deleteUser(email);

        Assert.assertTrue(user.isDeleted());
    }

    @Test
    public void testDeleteUserInvalidEmail()
    {
        User obj = new User();
        obj.setEmail("joker@gmail.com");

        when(userRepository.findByEmail(obj.getEmail())).thenReturn(Optional.empty());
        try{
            userService.deleteUser(obj.getEmail());
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"User Not Found with email "+ obj.getEmail());
        }
    }

    @Test
    public void testDeleteUserDeletedMail()
    {
        user.setDeleted(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        try{
            userService.deleteUser(email);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"User Not Found with email " + user.getEmail());
        }
    }

    @DataProvider(name = "InitializeUpdatePasswordDto")
    public Object[] InitializeUpdatePasswordDto()
    {
        updatePasswordDto = new UserUpdatePasswordDto();
        updatePasswordDto.setCurrentPassword("test@456");
        updatePasswordDto.setNewPassword("newpswd");
        return new Object[]{updatePasswordDto};
    }

    @Test(dataProvider = "InitializeUpdatePasswordDto")
    public void testUpdatePasswordValidData(UserUpdatePasswordDto userUpdatePasswordDto)
    {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userUpdatePasswordDto.getCurrentPassword(),"test@456"))
                .thenReturn(true);
        when(passwordEncoder.encode(userUpdatePasswordDto.getNewPassword())).
                thenReturn("newpswd");
        when(userRepository.save(user)).thenReturn(user);

        String result = userService.updatePassword(email,userUpdatePasswordDto);

        Assert.assertEquals(result,"Your password has been updated!");
        Assert.assertEquals(user.getPassword(),"newpswd");
    }

    @Test(dataProvider = "InitializeUpdatePasswordDto")
    public void testUpdatePasswordInvalidCurrentPassword(UserUpdatePasswordDto updatePasswordDto)
    {
        updatePasswordDto.setCurrentPassword("random231");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(updatePasswordDto.getCurrentPassword(),"test@456"))
                .thenReturn(false);

        String result = userService.updatePassword(email,updatePasswordDto);

        Assert.assertEquals(result,"Old password does not match with current password");
    }

    @Test(dataProvider = "InitializeUpdatePasswordDto")
    public void testUpdatePasswordDeletedUser(UserUpdatePasswordDto updatePasswordDto)
    {
        user.setDeleted(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        String result = userService.updatePassword(email,updatePasswordDto);

        Assert.assertEquals(result,"User does not exists. Please register from the home page!");
    }

    @Test(dataProvider = "InitializeUpdatePasswordDto")
    public void testUpdatePasswordInvalidUser(UserUpdatePasswordDto updatePasswordDto){
        User obj = new User();
        obj.setEmail("randome@gmail.com");
        when(userRepository.findByEmail(obj.getEmail())).thenReturn(Optional.empty());

        String result = userService.updatePassword(obj.getEmail(),updatePasswordDto);

        Assert.assertEquals(result,"User does not exists. Please register from the home page!");
    }
}
