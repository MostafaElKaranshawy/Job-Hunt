package com.software.backend.dto;

import lombok.Data;
@Data
public class ApplicantDTO {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String country;
}
