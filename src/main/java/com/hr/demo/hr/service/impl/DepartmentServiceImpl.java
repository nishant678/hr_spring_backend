package com.hr.demo.hr.service.impl;

import com.hr.demo.hr.dto.CreateDepartmentRequest;
import com.hr.demo.hr.dto.UpdateDepartmentRequest;
import com.hr.demo.hr.entity.CompanyEntity;
import com.hr.demo.hr.entity.DepartmentEntity;
import com.hr.demo.exceptions.BadRequestException;
import com.hr.demo.exceptions.DuplicateResourceException;
import com.hr.demo.exceptions.ResourceNotFoundException;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.DepartmentResponse;
import com.hr.demo.hr.repository.CompanyRepository;
import com.hr.demo.hr.repository.DepartmentRepository;
import com.hr.demo.hr.service.DepartmentService;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;

    private DepartmentResponse map(DepartmentEntity department) {
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.isActive(),
                department.getCompany().getId());
    }

    @Override
    public DepartmentResponse createDepartment(CreateDepartmentRequest request, Long companyId) {
        String name = request.getName() == null ? null : request.getName().trim();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Department name is required");
        }

        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (departmentRepository.existsByNameIgnoreCaseAndCompanyId(name, companyId)) {
            throw new DuplicateResourceException("Department already exists in this company");
        }

        DepartmentEntity department = DepartmentEntity.builder()
                .name(name)
                .description(request.getDescription())
                .active(true)
                .company(company)
                .build();

        return map(departmentRepository.save(department));
    }

    @Override
    public DepartmentResponse updateDepartment(Long departmentId, UpdateDepartmentRequest request, Long companyId) {
        DepartmentEntity department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        if (!department.getCompany().getId().equals(companyId)) {
            throw new UnauthorizedException("Access denied");
        }

        String name = request.getName() == null ? null : request.getName().trim();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Department name is required");
        }

        if (!department.getName().equalsIgnoreCase(name)
                && departmentRepository.existsByNameIgnoreCaseAndCompanyId(name, companyId)) {
            throw new DuplicateResourceException("Department already exists in this company");
        }

        department.setName(name);
        department.setDescription(request.getDescription());

        return map(departmentRepository.save(department));
    }

    @Override
    public void deleteDepartment(Long departmentId, Long companyId) {
        DepartmentEntity department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        if (!department.getCompany().getId().equals(companyId)) {
            throw new UnauthorizedException("Access denied");
        }

        departmentRepository.deleteById(departmentId);
    }

    @Override
    public DepartmentResponse getDepartment(Long departmentId, Long companyId) {
        DepartmentEntity department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        if (!department.getCompany().getId().equals(companyId)) {
            throw new UnauthorizedException("Access denied");
        }

        return map(department);
    }

    @Override
    public List<DepartmentResponse> getCompanyDepartments(Long companyId) {
        return departmentRepository.findAllByCompanyId(companyId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DepartmentResponse> listDepartments(Long companyId, int page, int size, String search,
            String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection == null ? "ASC" : sortDirection),
                sortBy == null ? "name" : sortBy);
        var pageable = PageRequest.of(page, size, sort);
        var pageData = departmentRepository.searchByCompany(companyId, search, pageable);
        return new PageImpl<>(pageData.getContent().stream().map(this::map).collect(Collectors.toList()), pageable,
                pageData.getTotalElements());
    }

    @Override
    public DepartmentResponse setStatus(Long departmentId, boolean active, Long companyId) {
        DepartmentEntity department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        if (!department.getCompany().getId().equals(companyId)) {
            throw new UnauthorizedException("Access denied");
        }

        department.setActive(active);
        return map(departmentRepository.save(department));
    }
}
