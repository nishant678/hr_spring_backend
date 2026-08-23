package com.hr.demo.reaponse;

import com.hr.demo.domain.expense.ExpenseStatus;
import com.hr.demo.domain.expense.ExpenseType;
import com.hr.demo.hr.entity.ExpenseEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ExpenseResponse {

    private final Long id;
    private final ExpenseType expenseType;
    private final BigDecimal amount;
    private final LocalDate expenseDate;
    private final String description;
    private final String receiptUrl;
    private final ExpenseStatus status;
    private final String rejectionReason;
    private final Long userId;
    private final String userEmail;
    private final String userName;
    private final Long companyId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ExpenseResponse(ExpenseEntity expense) {
        this.id = expense.getId();
        this.expenseType = expense.getExpenseType();
        this.amount = expense.getAmount();
        this.expenseDate = expense.getExpenseDate();
        this.description = expense.getDescription();
        this.receiptUrl = expense.getReceiptUrl();
        this.status = expense.getStatus();
        this.rejectionReason = expense.getRejectionReason();
        this.userId = expense.getUser().getId();
        this.userEmail = expense.getUser().getEmail();
        this.userName = (expense.getUser().getFirstName() != null ? expense.getUser().getFirstName() : "")
                + " " + (expense.getUser().getLastName() != null ? expense.getUser().getLastName() : "");
        this.companyId = expense.getCompany().getId();
        this.createdAt = expense.getCreatedAt();
        this.updatedAt = expense.getUpdatedAt();
    }
}
