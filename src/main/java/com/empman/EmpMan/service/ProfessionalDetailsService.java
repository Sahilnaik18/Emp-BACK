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

//    public ProfessionalDetails addProfessionalDetails(ProfessionalDetails details, Long adminId, Long employeeId) {
//        Admin admin = adminRepository.findById(adminId)
//                .orElseThrow(() -> new RuntimeException("Admin not found"));
//
//        Employee employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new RuntimeException("Employee not found"));
//
//        details.setAdmin(admin);
//        details.setEmployee(employee);
//
//        return professionalDetailsRepository.save(details);
//    }
public ProfessionalDetails addProfessionalDetails(ProfessionalDetails details, Long adminId, Long employeeId) {
    Admin admin;
    Employee employee;

    try {
        admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    } catch (Exception e) {
        // Log the error and rethrow
        System.err.println("Error fetching Admin or Employee: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }

    details.setAdmin(admin);
    details.setEmployee(employee);

    try {
        return professionalDetailsRepository.save(details);
    } catch (Exception e) {
        // Log the error and rethrow
        System.err.println("Error saving ProfessionalDetails: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }
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

    // ✅ Fix Update Logic
    public ProfessionalDetails updateProfessionalDetails(Long employeeId, ProfessionalDetails details) {
        List<ProfessionalDetails> existingDetails = professionalDetailsRepository.findByEmployeeId(employeeId);

        if (existingDetails.isEmpty()) {
            throw new RuntimeException("Professional details not found for employeeId: " + employeeId);
        }

        ProfessionalDetails professionalDetails = existingDetails.get(0); // Assuming one record per employee
        professionalDetails.setEmploymentCode(details.getEmploymentCode());
        professionalDetails.setCompanyMail(details.getCompanyMail());
        professionalDetails.setOfficePhone(details.getOfficePhone());
        professionalDetails.setCity(details.getCity());
        professionalDetails.setOfficeAddressLine1(details.getOfficeAddressLine1());
        professionalDetails.setOfficeAddressLine2(details.getOfficeAddressLine2());
        professionalDetails.setPincode(details.getPincode());
        professionalDetails.setReportingManager(details.getReportingManager());
        professionalDetails.setHrName(details.getHrName());
        professionalDetails.setDateOfJoining(details.getDateOfJoining());
        professionalDetails.setEmploymentHistory(details.getEmploymentHistory());

        return professionalDetailsRepository.save(professionalDetails);
    }

    // ✅ Fix Delete Logic
    public void deleteProfessionalDetails(Long employeeId) {
        List<ProfessionalDetails> detailsList = professionalDetailsRepository.findByEmployeeId(employeeId);
        if (detailsList.isEmpty()) {
            throw new RuntimeException("No professional details found for employeeId: " + employeeId);
        }

        professionalDetailsRepository.deleteAll(detailsList);
    }
}
