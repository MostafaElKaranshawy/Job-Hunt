package com.software.backend.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum JobReportReason {
    INAPPROPRIATE_CONTENT,
    SCAM,
    DISCRIMINATION,
    ILLEGAL_ACTIVITY,
    OTHER
}