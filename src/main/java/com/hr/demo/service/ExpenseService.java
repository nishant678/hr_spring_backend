package com.hr.demo.service;

import com.hr.demo.dto.CreateExpenseRequest;
import com.hr.demo.dto.RejectExpenseRequest;
import com.hr.demo.reaponse.ExpenseResponse;

import java.util.List;

public interface ExpenseService {

    ExpenseResponse createExpense(CreateExpenseRequest request, Long userId, Long companyId);

    void uploadReceipt(Long expenseId, String receiptUrl);

    List<ExpenseResponse> getMyExpenses(Long userId);

    List<ExpenseResponse> getAllCompanyExpenses(Long companyId);

    ExpenseResponse approveExpense(Long expenseId, Long adminId);

    ExpenseResponse rejectExpense(Long expenseId, RejectExpenseRequest request, Long adminId);

    ExpenseResponse getExpense(Long expenseId);
}
