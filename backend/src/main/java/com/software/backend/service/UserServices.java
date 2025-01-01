package com.software.backend.service;

import com.software.backend.dto.ChangePasswordDto;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.User;
import com.software.backend.exception.InvalidCredentialsException;
import com.software.backend.exception.UserNotFoundException;
import com.software.backend.repository.UserRepository;
import com.software.backend.validation.validators.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServices {
    private final UserRepository repo;

    private final PasswordService passwordService;

    public User getUser(String username) {
        return this.repo.findByUsername(username).orElse(null);
    }

    public void changePassword(ChangePasswordDto changePasswordDto) {
        User user = this.repo.findByUsername(changePasswordDto.getUserName()).orElse(null);
        if(user != null) {
            System.out.println("user found");
            changePasswordDto.setOldPassword(passwordService.hashPassword(changePasswordDto.getOldPassword()));
            if(user.getPassword().equals(changePasswordDto.getOldPassword())) {
                System.out.println("old password matched");
                PasswordValidator passwordValidator = new PasswordValidator();
                SignUpRequest signUpRequest = new SignUpRequest();
                signUpRequest.setPassword(changePasswordDto.getNewPassword());
                passwordValidator.validate(signUpRequest);
                System.out.println("password validated");
                changePasswordDto.setNewPassword(passwordService.hashPassword(changePasswordDto.getNewPassword()));
                user.setPassword(changePasswordDto.getNewPassword());
                this.repo.save(user);
                System.out.println("password changed");
            }
            else{
                throw new InvalidCredentialsException("Old password is incorrect");
            }
        }
        else{
            throw new UserNotFoundException("User not found");
        }
    }
}
