package com.empman.EmpMan.service;

import com.empman.EmpMan.Entities.Admin;
import com.empman.EmpMan.Entities.Employee;
import com.empman.EmpMan.Entities.ProfessionalDetails;
import com.empman.EmpMan.repository.AdminRepository;
import com.empman.EmpMan.repository.EmployeeRepository;
import com.empman.EmpMan.repository.ProfessionalDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessionalDetailsService {

    @Autowired
    private ProfessionalDetailsRepository professionalDetailsRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public ProfessionalDetails addProfessionalDetails(ProfessionalDetails details, Long adminId, Long employeeId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        details.setAdmin(admin);
        details.setEmployee(employee);

        return professionalDetailsRepository.save(details);
    }

    public List<ProfessionalDetails> getAllProfessionalDetails() {
        return professionalDetailsRepository.findAll();
    }

    public List<ProfessionalDetails> getProfessionalDetailsByEmployee(Long employeeId) {
        return professionalDetailsRepository.findByEmployeeId(employeeId);
    }

    public List<ProfessionalDetails> getProfessionalDetailsByAdmin(Long adminId) {
        return professionalDetailsRepository.findByAdminId(adminId);
    }

    public void deleteProfessionalDetails(Long id) {
        professionalDetailsRepository.deleteById(id);
    }
}
