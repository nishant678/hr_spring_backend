package com.hr.demo.dto;
import com.hr.demo.domain.company.CompanyStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyResponse {
    private Long id;
    private String companyName;
    private String email;
    private CompanyStatus status;
}