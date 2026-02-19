package com.hr.demo.controller;

import com.hr.demo.dto.CreateRoleRequest;
import com.hr.demo.dto.UpdateRoleRequest;
import com.hr.demo.reaponse.RoleResponse;
import com.hr.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // CREATE ROLE
    @PostMapping
    public ResponseEntity<RoleResponse> create(@RequestBody CreateRoleRequest request){
        return ResponseEntity.ok(roleService.createRole(request));
    }

    // UPDATE ROLE
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> update(
            @PathVariable Long id,
            @RequestBody UpdateRoleRequest request){
        return ResponseEntity.ok(roleService.updateRole(id, request));
    }

    // DELETE ROLE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }

    // GET SINGLE ROLE
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> get(@PathVariable Long id){
        return ResponseEntity.ok(roleService.getRole(id));
    }

    // GET COMPANY ROLES
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<RoleResponse>> getCompanyRoles(@PathVariable Long companyId){
        return ResponseEntity.ok(roleService.getCompanyRoles(companyId));
    }
}