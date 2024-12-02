package com.software.backend.controller;

import com.software.backend.entity.User;
import com.software.backend.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "user")
@CrossOrigin
public class UserController {
    @Autowired
    UserServices service;
    @GetMapping("/{username}")
    public ResponseEntity<User> getApplicant(@PathVariable String username){
        User user = service.getUser(username);
        if(user != null)
            return new ResponseEntity<>(user, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
