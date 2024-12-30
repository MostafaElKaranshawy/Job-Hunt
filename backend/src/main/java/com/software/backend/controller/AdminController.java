package com.software.backend.controller;


import com.software.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @PatchMapping("/{username}/ban")
    public ResponseEntity<?> banUser(@PathVariable String username){
        if (adminService.banUser(username))
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/reports")
    public ResponseEntity<?> unbanUser(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "offset", defaultValue = "5") Integer offset
    ){
        return ResponseEntity.ok(adminService.getReports(page, offset));
    }



//
//    @GetMapping("/reports")
//    public ResponseEntity<Page<JobReportDTO>> getReports(
//            @RequestParam(name = "page", defaultValue = "0") Integer page,
//            @RequestParam(name = "offset", defaultValue = "5") Integer offset) {
//
//        Page<JobReportDTO> reports = adminService.getReports(page, offset)
//                .map(JobReportDTO::fromEntity);
//        return ResponseEntity.ok(reports);
//    }
}
