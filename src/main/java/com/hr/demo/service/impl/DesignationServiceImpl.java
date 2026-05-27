package com.hr.demo.service.impl;

import com.hr.demo.dto.CreateDesignationRequest;
import com.hr.demo.dto.UpdateDesignationRequest;
import com.hr.demo.entity.CompanyEntity;
import com.hr.demo.entity.DesignationEntity;
import com.hr.demo.exceptions.BadRequestException;
import com.hr.demo.exceptions.DuplicateResourceException;
import com.hr.demo.exceptions.ResourceNotFoundException;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.DesignationResponse;
import com.hr.demo.repository.CompanyRepository;
import com.hr.demo.repository.DesignationRepository;
import com.hr.demo.repository.DepartmentRepository;
import com.hr.demo.service.DesignationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DesignationServiceImpl implements DesignationService {

    private final DesignationRepository designationRepository;
    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;

    private DesignationResponse map(DesignationEntity designation) {
        return new DesignationResponse(
                designation.getId(),
                designation.getName(),
                designation.getDescription(),
                designation.isActive(),
                designation.getCompany().getId(),
                designation.getDepartment() == null ? null : designation.getDepartment().getId());
    }

    @Override
    public DesignationResponse createDesignation(CreateDesignationRequest request, Long companyId) {
        String name = request.getName() == null ? null : request.getName().trim();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Designation name is required");
        }

        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (designationRepository.existsByNameIgnoreCaseAndCompanyId(name, companyId)) {
            throw new DuplicateResourceException("Designation already exists in this company");
        }

        if (request.getDepartmentId() == null) {
            throw new BadRequestException("Department id is required");
        }

        var department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        if (!department.getCompany().getId().equals(companyId)) {
            throw new BadRequestException("Department does not belong to the company");
        }

        DesignationEntity designation = DesignationEntity.builder()
                .name(name)
                .description(request.getDescription())
                .active(true)
                .company(company)
                .department(department)
                .build();

        return map(designationRepository.save(designation));
    }

    @Override
    public DesignationResponse updateDesignation(Long designationId, UpdateDesignationRequest request, Long companyId) {
        DesignationEntity designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found"));

        if (!designation.getCompany().getId().equals(companyId)) {
            throw new UnauthorizedException("Access denied");
        }

        String name = request.getName() == null ? null : request.getName().trim();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Designation name is required");
        }

        if (!designation.getName().equalsIgnoreCase(name)
                && designationRepository.existsByNameIgnoreCaseAndCompanyId(name, companyId)) {
            throw new DuplicateResourceException("Designation already exists in this company");
        }

        designation.setName(name);
        designation.setDescription(request.getDescription());

        if (request.getDepartmentId() != null) {
            var department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            if (!department.getCompany().getId().equals(companyId)) {
                throw new BadRequestException("Department does not belong to the company");
            }
            designation.setDepartment(department);
        }

        return map(designationRepository.save(designation));
    }

    @Override
    public void deleteDesignation(Long designationId, Long companyId) {
        DesignationEntity designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found"));

        if (!designation.getCompany().getId().equals(companyId)) {
            throw new UnauthorizedException("Access denied");
        }

        designationRepository.deleteById(designationId);
    }

    @Override
    public DesignationResponse getDesignation(Long designationId, Long companyId) {
        DesignationEntity designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found"));

        if (!designation.getCompany().getId().equals(companyId)) {
            throw new UnauthorizedException("Access denied");
        }

        return map(designation);
    }

    @Override
    public List<DesignationResponse> getCompanyDesignations(Long companyId) {
        return designationRepository.findAllByCompanyId(companyId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DesignationResponse> listDesignations(Long companyId, int page, int size, String search,
            String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection == null ? "ASC" : sortDirection),
                sortBy == null ? "name" : sortBy);
        var pageable = PageRequest.of(page, size, sort);
        var pageData = designationRepository.searchByCompany(companyId, search, pageable);
        return new PageImpl<>(pageData.getContent().stream().map(this::map).collect(Collectors.toList()), pageable,
                pageData.getTotalElements());
    }

    @Override
    public DesignationResponse setStatus(Long designationId, boolean active, Long companyId) {
        DesignationEntity designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found"));

        if (!designation.getCompany().getId().equals(companyId)) {
            throw new UnauthorizedException("Access denied");
        }

        designation.setActive(active);
        return map(designationRepository.save(designation));
    }
}
