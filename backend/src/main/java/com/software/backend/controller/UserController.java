package com.software.backend.controller;

import com.software.backend.dto.ChangePasswordDto;
import com.software.backend.entity.User;
import com.software.backend.service.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")

public class UserController {
    @Autowired
    UserServices userServices;
    @CrossOrigin
    @GetMapping("/{username}")
    public ResponseEntity<User> getApplicant(
            @PathVariable String username
    ){
        System.out.println("Username: " + username);
        User user = userServices.getUser(username);
        if(user != null)
            return new ResponseEntity<>(user, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @CrossOrigin
    @PutMapping("/{userName}/profile/password")
    public ResponseEntity<String> changePassword(
            @PathVariable String userName,
            @RequestBody ChangePasswordDto changePasswordDto
            ){
        changePasswordDto.setUserName(userName);
        try {
            userServices.changePassword(changePasswordDto);
            return ResponseEntity.ok().body("Password changed successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }





}
