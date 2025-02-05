package com.empman.EmpMan.service;

import com.empman.EmpMan.Entities.Admin;
import com.empman.EmpMan.Entities.Employee;
import com.empman.EmpMan.Entities.Finance;
import com.empman.EmpMan.repository.AdminRepository;
import com.empman.EmpMan.repository.EmployeeRepository;
import com.empman.EmpMan.repository.FinanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceService {

    @Autowired
    private FinanceRepository financeRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Finance addFinanceRecord(Finance finance, Long adminId, Long employeeId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        finance.setAdmin(admin);

        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            finance.setEmployee(employee);
        }

        return financeRepository.save(finance);
    }

    public List<Finance> getAllFinanceRecords() {
        return financeRepository.findAll();
    }

    public List<Finance> getFinanceByEmployee(Long employeeId) {
        return financeRepository.findByEmployeeId(employeeId);
    }

    public List<Finance> getFinanceByAdmin(Long adminId) {
        return financeRepository.findByAdminId(adminId);
    }

    public Finance updateFinanceRecord(Long id, Finance financeDetails) {
        Finance finance = financeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Finance record not found"));

        finance.setType(financeDetails.getType());
        finance.setAmount(financeDetails.getAmount());
        finance.setDescription(financeDetails.getDescription());
        finance.setDate(financeDetails.getDate());

        // ✅ Ensure CTC is updated
        if (financeDetails.getCtc() != null) {
            finance.setCtc(financeDetails.getCtc());
        }

        return financeRepository.save(finance);
    }


    public void deleteFinanceRecord(Long id) {
        financeRepository.deleteById(id);
    }
}
