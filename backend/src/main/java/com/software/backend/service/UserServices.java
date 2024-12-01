package com.software.backend.service;

import com.software.backend.entity.User;
import com.software.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {
    @Autowired
    UserRepository repo;
    public User getUser(String username) {
        return this.repo.findByUsername(username).orElse(null);
    }
}
