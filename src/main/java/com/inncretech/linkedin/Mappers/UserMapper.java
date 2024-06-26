package com.inncretech.linkedin.Mappers;

import com.inncretech.linkedin.DTOs.UserRegistrationDto;
import com.inncretech.linkedin.Models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserRegistrationDto userToUserRegistrationDto(User user)
    {
        if(user == null)
            return null;
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();

        userRegistrationDto.setFirstName(user.getFirstName());
        userRegistrationDto.setLastName(user.getLastName());
        userRegistrationDto.setEmail(user.getEmail());
        userRegistrationDto.setPassword(user.getPassword());
        userRegistrationDto.setMobile(user.getMobile());

        return userRegistrationDto;
    }

    public User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto) {
        if (userRegistrationDto == null) return null;

        User user = new User();
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(userRegistrationDto.getPassword());
        user.setMobile(userRegistrationDto.getMobile());

        return user;
    }
}
