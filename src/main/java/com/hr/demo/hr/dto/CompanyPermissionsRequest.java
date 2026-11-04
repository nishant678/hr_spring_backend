package com.hr.demo.hr.dto;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;

public record CompanyPermissionsRequest(
        @NotNull @JsonProperty("face_attendense") @JsonAlias("faceVerificationAttendance") Boolean faceVerificationAttendance,
        Boolean task) {}
