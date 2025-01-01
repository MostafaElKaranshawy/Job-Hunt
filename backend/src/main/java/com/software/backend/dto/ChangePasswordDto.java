package com.software.backend.dto;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private String userName;
    private String oldPassword;
    private String newPassword;
}
