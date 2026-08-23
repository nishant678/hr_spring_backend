package com.hr.demo.hr.service.impl;

import com.hr.demo.domain.expense.ExpenseStatus;
import com.hr.demo.hr.dto.CreateExpenseRequest;
import com.hr.demo.hr.dto.RejectExpenseRequest;
import com.hr.demo.hr.entity.CompanyEntity;
import com.hr.demo.hr.entity.ExpenseEntity;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.exceptions.BadRequestException;
import com.hr.demo.exceptions.ResourceNotFoundException;
import com.hr.demo.reaponse.ExpenseResponse;
import com.hr.demo.hr.repository.CompanyRepository;
import com.hr.demo.hr.repository.ExpenseRepository;
import com.hr.demo.hr.repository.UserRepository;
import com.hr.demo.hr.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Override
    public ExpenseResponse createExpense(CreateExpenseRequest request, Long userId, Long companyId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        ExpenseEntity expense = ExpenseEntity.builder()
                .expenseType(request.getExpenseType())
                .amount(request.getAmount())
                .expenseDate(request.getExpenseDate())
                .description(request.getDescription())
                .status(ExpenseStatus.PENDING)
                .user(user)
                .company(company)
                .build();

        expenseRepository.save(expense);
        return new ExpenseResponse(expense);
    }

    @Override
    public void uploadReceipt(Long expenseId, String receiptUrl) {
        ExpenseEntity expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        expense.setReceiptUrl(receiptUrl);
        expenseRepository.save(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getMyExpenses(Long userId) {
        return expenseRepository.findByUser_IdOrderByCreatedAtDesc(userId)
                .stream().map(ExpenseResponse::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getAllCompanyExpenses(Long companyId) {
        return expenseRepository.findByCompany_IdOrderByCreatedAtDesc(companyId)
                .stream().map(ExpenseResponse::new).toList();
    }

    @Override
    public ExpenseResponse approveExpense(Long expenseId, Long adminId) {
        ExpenseEntity expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new BadRequestException("Expense is already " + expense.getStatus().name().toLowerCase());
        }

        expense.setStatus(ExpenseStatus.APPROVED);
        expenseRepository.save(expense);
        return new ExpenseResponse(expense);
    }

    @Override
    public ExpenseResponse rejectExpense(Long expenseId, RejectExpenseRequest request, Long adminId) {
        ExpenseEntity expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new BadRequestException("Expense is already " + expense.getStatus().name().toLowerCase());
        }

        expense.setStatus(ExpenseStatus.REJECTED);
        expense.setRejectionReason(request.getRejectionReason());
        expenseRepository.save(expense);
        return new ExpenseResponse(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse getExpense(Long expenseId) {
        ExpenseEntity expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        return new ExpenseResponse(expense);
    }
}
