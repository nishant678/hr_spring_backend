package com.hr.demo.hr.service.impl;

import com.hr.demo.domain.attendance.AttendanceStatus;
import com.hr.demo.exceptions.BadRequestException;
import com.hr.demo.exceptions.ResourceNotFoundException;
import com.hr.demo.hr.entity.*;
import com.hr.demo.hr.repository.*;
import com.hr.demo.reaponse.PayrollResponse;
import com.hr.demo.reaponse.PayrollSummaryResponse;
import com.hr.demo.hr.service.PayrollService;
import com.hr.demo.util.NumberToWords;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRepository leaveRepository;
    private final HolidayRepository holidayRepository;

    @Override
    public List<PayrollResponse> processPayroll(Long companyId, int year, int month) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        List<UserEntity> employees = userRepository.findByCompany_Id(companyId);
        if (employees.isEmpty()) {
            throw new BadRequestException("No employees found in this company");
        }

        int workingDays = calculateWorkingDays(companyId, year, month);
        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
        String monthName = getMonthName(month);

        // Delete existing payroll records for this month to allow reprocessing
        List<PayrollEntity> existing = payrollRepository.findByCompany_IdAndYearAndMonthOrderByUser_IdAsc(companyId, year, month);
        payrollRepository.deleteAll(existing);

        List<PayrollResponse> responses = new ArrayList<>();

        for (UserEntity emp : employees) {
            if (emp.getBasicSalary() == null || emp.getGrossSalary() == null) continue;

            List<AttendanceEntity> attendanceList = attendanceRepository
                    .findByUser_IdAndDateBetween(emp.getId(), monthStart, monthEnd);

            int presentCount = 0, lateCount = 0, halfCount = 0;
            for (AttendanceEntity a : attendanceList) {
                if (a.getStatus() == AttendanceStatus.PRESENT) presentCount++;
                else if (a.getStatus() == AttendanceStatus.LATE) lateCount++;
                else if (a.getStatus() == AttendanceStatus.HALF_DAY) halfCount++;
            }

            List<LeaveEntity> leaves = leaveRepository
                    .findApprovedLeavesByUserAndMonth(emp.getId(), monthStart, monthEnd);
            int paidLeaveDays = 0;
            for (LeaveEntity l : leaves) {
                LocalDate start = l.getFromDate().isBefore(monthStart) ? monthStart : l.getFromDate();
                LocalDate end = l.getToDate().isAfter(monthEnd) ? monthEnd : l.getToDate();
                paidLeaveDays += (int) ChronoUnit.DAYS.between(start, end) + 1;
            }

            int attendanceDays = presentCount + lateCount + halfCount;
            int accountedDays = attendanceDays + paidLeaveDays;
            int absentDays = Math.max(0, workingDays - accountedDays);
            double payableDays = presentCount + lateCount + (halfCount * 0.5) + paidLeaveDays;
            double payableRatio = workingDays > 0 ? Math.min(1.0, payableDays / workingDays) : 0.0;

            BigDecimal basic = emp.getBasicSalary();
            BigDecimal gross = emp.getGrossSalary();

            BigDecimal proRataBasic = basic.multiply(BigDecimal.valueOf(payableRatio)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal proRataGross = gross.multiply(BigDecimal.valueOf(payableRatio)).setScale(2, RoundingMode.HALF_UP);

            BigDecimal hra = proRataBasic.multiply(BigDecimal.valueOf(0.50)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal conveyance = BigDecimal.valueOf(1600)
                    .multiply(BigDecimal.valueOf(payableRatio)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal medical = BigDecimal.valueOf(1250)
                    .multiply(BigDecimal.valueOf(payableRatio)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal specialAllowance = proRataGross.subtract(proRataBasic)
                    .subtract(hra).subtract(conveyance).subtract(medical)
                    .max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);

            BigDecimal pf = proRataBasic.multiply(BigDecimal.valueOf(0.12)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal esi = proRataGross.compareTo(BigDecimal.valueOf(21000)) <= 0
                    ? proRataGross.multiply(BigDecimal.valueOf(0.0075)).setScale(2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
            BigDecimal professionalTax = BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP);
            BigDecimal tds = BigDecimal.ZERO;
            BigDecimal totalDeductions = pf.add(esi).add(professionalTax).add(tds);

            BigDecimal netPay = proRataGross.subtract(totalDeductions).setScale(2, RoundingMode.HALF_UP);
            String netPayInWords = "Rupees " + NumberToWords.convert(netPay.longValue()) + " Only";

            PayrollEntity payroll = PayrollEntity.builder()
                    .company(company).user(emp)
                    .year(year).month(month).monthName(monthName)
                    .totalWorkingDays(workingDays)
                    .presentDays(presentCount).lateDays(lateCount).halfDays(halfCount)
                    .absentDays(absentDays).paidLeaveDays(paidLeaveDays).unpaidLeaveDays(0)
                    .basicSalary(proRataBasic).hra(hra).conveyance(conveyance).medical(medical)
                    .specialAllowance(specialAllowance).grossEarnings(proRataGross)
                    .pf(pf).esi(esi).professionalTax(professionalTax).tds(tds)
                    .totalDeductions(totalDeductions).netPay(netPay).netPayInWords(netPayInWords)
                    .status("PROCESSED").processedDate(LocalDate.now())
                    .bankName(emp.getBankName()).bankAccountNumber(emp.getBankAccountNumber())
                    .ifscCode(emp.getIfscCode())
                    .build();

            payrollRepository.save(payroll);
            responses.add(new PayrollResponse(payroll));
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollResponse> getPayrollRecords(Long companyId, int year, int month) {
        return payrollRepository.findByCompany_IdAndYearAndMonthOrderByUser_IdAsc(companyId, year, month)
                .stream().map(PayrollResponse::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PayrollResponse getPayrollRecord(Long payrollId) {
        PayrollEntity entity = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll record not found"));
        return new PayrollResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PayrollResponse getMyPayrollRecord(Long userId, int year, int month) {
        PayrollEntity entity = payrollRepository.findByUser_IdAndYearAndMonth(userId, year, month)
                .orElse(null);
        if (entity == null) return null;
        return new PayrollResponse(entity);
    }

    @Override
    public PayrollResponse markAsPaid(Long payrollId) {
        PayrollEntity entity = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll record not found"));
        entity.setStatus("PAID");
        entity.setPaidDate(LocalDate.now());
        payrollRepository.save(entity);
        return new PayrollResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PayrollSummaryResponse getSummary(Long companyId, int year, int month) {
        List<PayrollEntity> records = payrollRepository
                .findByCompany_IdAndYearAndMonthOrderByUser_IdAsc(companyId, year, month);

        long totalEmployees = userRepository.countByCompany_Id(companyId);
        int processedCount = records.size();
        int paidCount = (int) records.stream().filter(r -> "PAID".equals(r.getStatus())).count();
        BigDecimal totalGross = records.stream().map(PayrollEntity::getGrossEarnings)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDeductions = records.stream().map(PayrollEntity::getTotalDeductions)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalNet = records.stream().map(PayrollEntity::getNetPay)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PayrollSummaryResponse(totalEmployees, processedCount, paidCount,
                totalGross, totalDeductions, totalNet);
    }

    private int calculateWorkingDays(Long companyId, int year, int month) {
        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

        List<HolidayEntity> holidays = holidayRepository
                .findByCompany_IdAndDateBetweenOrderByDateAsc(companyId, monthStart, monthEnd);
        Set<LocalDate> holidayDates = holidays.stream().map(HolidayEntity::getDate).collect(Collectors.toSet());

        int count = 0;
        LocalDate d = monthStart;
        while (!d.isAfter(monthEnd)) {
            if (d.getDayOfWeek() != DayOfWeek.SATURDAY && d.getDayOfWeek() != DayOfWeek.SUNDAY
                    && !holidayDates.contains(d)) {
                count++;
            }
            d = d.plusDays(1);
        }
        return count;
    }

    private String getMonthName(int m) {
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        return (m >= 1 && m <= 12) ? months[m - 1] : "Unknown";
    }
}
