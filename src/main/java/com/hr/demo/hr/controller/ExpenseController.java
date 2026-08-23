package com.hr.demo.hr.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.hr.dto.CreateExpenseRequest;
import com.hr.demo.hr.dto.RejectExpenseRequest;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.ExpenseResponse;
import com.hr.demo.hr.service.ExpenseService;
import com.hr.demo.hr.service.FileStorageService;
import com.hr.demo.util.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final FileStorageService fileStorageService;
    private final SecurityUtil securityUtil;

    private UserEntity getCurrentUser() {
        return securityUtil.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated"));
    }

    private Long currentUserId() {
        return getCurrentUser().getId();
    }

    private Long currentCompanyId() {
        UserEntity user = getCurrentUser();
        if (user.getCompany() == null) {
            throw new UnauthorizedException("User not linked to a company");
        }
        return user.getCompany().getId();
    }

    @PostMapping("/api/expenses")
    public ResponseEntity<ApiResponse<ExpenseResponse>> createExpense(
            @Valid @RequestBody CreateExpenseRequest request) {
        var response = expenseService.createExpense(request, currentUserId(), currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Expense submitted successfully", response));
    }

    @PostMapping("/api/expenses/{id}/receipt")
    public ResponseEntity<ApiResponse<ExpenseResponse>> uploadReceipt(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String filePath = fileStorageService.storeFile(file, currentCompanyId(), id);
        expenseService.uploadReceipt(id, filePath);
        var expense = expenseService.getExpense(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Receipt uploaded", expense));
    }

    @GetMapping("/api/expenses/my")
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> myExpenses() {
        var expenses = expenseService.getMyExpenses(currentUserId());
        return ResponseEntity.ok(new ApiResponse<>(true, "My expenses fetched", expenses));
    }

    @GetMapping("/api/expenses")
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> allExpenses() {
        var expenses = expenseService.getAllCompanyExpenses(currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "All expenses fetched", expenses));
    }

    @GetMapping("/api/expenses/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> getExpense(@PathVariable Long id) {
        var expense = expenseService.getExpense(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expense fetched", expense));
    }

    @PutMapping("/api/expenses/{id}/approve")
    public ResponseEntity<ApiResponse<ExpenseResponse>> approveExpense(@PathVariable Long id) {
        var expense = expenseService.approveExpense(id, currentUserId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Expense approved", expense));
    }

    @PutMapping("/api/expenses/{id}/reject")
    public ResponseEntity<ApiResponse<ExpenseResponse>> rejectExpense(
            @PathVariable Long id,
            @Valid @RequestBody RejectExpenseRequest request) {
        var expense = expenseService.rejectExpense(id, request, currentUserId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Expense rejected", expense));
    }
}
