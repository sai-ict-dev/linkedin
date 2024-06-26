package com.inncretech.linkedin.Controller;

import com.inncretech.linkedin.DTOs.UserLoginDto;
import com.inncretech.linkedin.DTOs.UserRegistrationDto;
import com.inncretech.linkedin.DTOs.UserUpdateDto;
import com.inncretech.linkedin.DTOs.UserUpdatePasswordDto;
import com.inncretech.linkedin.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationDto> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto)
    {
        UserRegistrationDto userDto = userService.createUser(userRegistrationDto);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping("/getUserDetails")
    public ResponseEntity<UserRegistrationDto> getUserByEmail(@RequestParam(value = "email") String email)
    {

            UserRegistrationDto userRegistrationDto = userService.getUser(email);
            return new ResponseEntity<>(userRegistrationDto,HttpStatus.OK);

    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserRegistrationDto>> getAllUsers()
    {
        List<UserRegistrationDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserRegistrationDto> login(@Valid @RequestBody UserLoginDto userLoginDto)
    {
        UserRegistrationDto userRegistrationDto = userService.validateUser(userLoginDto);
        return new ResponseEntity<>(userRegistrationDto,HttpStatus.OK);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestParam String email,
                                                              @Valid @RequestBody UserUpdatePasswordDto userUpdatePasswordDto)

    {
        String message = userService.updatePassword(email,userUpdatePasswordDto);
        return new ResponseEntity<>(message,HttpStatus.OK);

    }

    @PutMapping("/updatePersonalDetails")
    public ResponseEntity<UserRegistrationDto> updatePersonalDetails(@RequestParam String email,
                                                                     @Valid @RequestBody UserUpdateDto userUpdateDto)
    {
            UserRegistrationDto userRegistrationDto = userService.updatePersonalDetails(email, userUpdateDto);
            return new ResponseEntity<>(userRegistrationDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> DeleteUser(@RequestParam String email)
    {
            userService.deleteUser(email);
            return new ResponseEntity<>("User Deleted",HttpStatus.OK);

    }


}
