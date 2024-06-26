package com.inncretech.linkedin.Services;

import com.inncretech.linkedin.DTOs.UserLoginDto;
import com.inncretech.linkedin.DTOs.UserRegistrationDto;
import com.inncretech.linkedin.DTOs.UserUpdateDto;
import com.inncretech.linkedin.DTOs.UserUpdatePasswordDto;
import com.inncretech.linkedin.Mappers.UserMapper;
import com.inncretech.linkedin.Models.User;
import com.inncretech.linkedin.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;



    public UserRegistrationDto createUser(UserRegistrationDto userRegistrationDto) {
        Optional<User> existingUser = userRepository.findByEmail(userRegistrationDto.getEmail());
        if(existingUser.isPresent() && !existingUser.get().isDeleted()) {
            throw new IllegalArgumentException("User already exists with the email: " + userRegistrationDto.getEmail());
        }

        String password = userRegistrationDto.getPassword();
        User user = userMapper.userRegistrationDtoToUser(userRegistrationDto);
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setDeleted(false);
        User saveduser = userRepository.save(user);

        UserRegistrationDto dto = userMapper.userToUserRegistrationDto(saveduser);
        dto.setPassword(password);
        return dto;
    }

    public List<UserRegistrationDto> getAllUsers()
    {
        List<User> users = userRepository.findAll();
        return users.stream().filter(user -> !user.isDeleted()).
                map(userMapper::userToUserRegistrationDto)
                .collect(Collectors.toList());
    }

    public UserRegistrationDto validateUser(UserLoginDto userLoginDto)
    {
        Optional<User> user = userRepository.findByEmail(userLoginDto.getUserName());
        if(!user.isPresent() || user.get().isDeleted())
        {
            throw new IllegalArgumentException("User does not found with the given email");
        }

        if(passwordEncoder.matches(userLoginDto.getPassword(),user.get().getPassword()))
        {
            // raw password and hashed password are matching
            UserRegistrationDto userRegistrationDto = userMapper.userToUserRegistrationDto(user.get());
            userRegistrationDto.setPassword(userLoginDto.getPassword());
            return userRegistrationDto;
        }

        throw new IllegalArgumentException("UserName or password incorrect. Please check and try again");
    }

    public UserRegistrationDto getUser(String email)
    {
        Optional<User> userFromDB = userRepository.findByEmail(email);
        if(userFromDB.isPresent() && !userFromDB.get().isDeleted())
        {
            User user = userFromDB.get();
            return userMapper.userToUserRegistrationDto(user);
        }
        else {
            throw new IllegalArgumentException("User Not Found with the mail " + email);
        }
    }

    public UserRegistrationDto updatePersonalDetails(String email, UserUpdateDto updateDto)
    {
        Optional<User> userFromDB = userRepository.findByEmail(email);
        if(userFromDB.isPresent() && !userFromDB.get().isDeleted())
        {
            User user = userFromDB.get();
            user.setFirstName(updateDto.getFirstName());
            user.setLastName(updateDto.getLastName());
            user.setMobile(updateDto.getMobile());
            user.setUpdatedAt(LocalDateTime.now());

            User updatedUser = userRepository.save(user);
            return userMapper.userToUserRegistrationDto(updatedUser);
        }
        else {
            throw new IllegalArgumentException("User not found with email " + email);
        }
    }

    public String updatePassword(String email, UserUpdatePasswordDto userUpdatePasswordDto)
    {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent() && !user.get().isDeleted())
        {
            if(passwordEncoder.matches(userUpdatePasswordDto.getCurrentPassword(),user.get().getPassword()))
            {
                String newPasswordHashed = passwordEncoder.encode(userUpdatePasswordDto.getNewPassword());
                user.get().setPassword(newPasswordHashed);
                userRepository.save(user.get());
                return "Your password has been updated!";
            }
            else{
                return "Old password does not match with current password";
            }
        }
        return "User does not exists. Please register from the home page!";
    }

    public void deleteUser(String email){
        Optional<User> userFromDB = userRepository.findByEmail(email);
        if(userFromDB.isPresent() && !userFromDB.get().isDeleted())
        {
            User user = userFromDB.get();
            user.setUpdatedAt(LocalDateTime.now());
            user.setDeleted(true);
            userRepository.save(user);
        }
        else {
            throw new IllegalArgumentException("User Not Found with email " + email);
        }
    }
}
