package com.software.backend.service;


import com.software.backend.entity.JobReport;
import com.software.backend.entity.User;
import com.software.backend.exception.UserNotFoundException;
import com.software.backend.repository.JobReportRepository;
import com.software.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobReportRepository jobReportRepository;

    public boolean banUser(String username) {

        User user =  userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User not found with username: " + username)
        );

        user.setIsBanned(true);
        userRepository.save(user);
        return true;
    }

    public Page<JobReport> getReports(Integer page, Integer offset) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, offset, sort);

        jobReportRepository.findAll(pageable);

        return jobReportRepository.findAll(pageable);
    }
}
