package com.empman.EmpMan.controller;


import com.empman.EmpMan.Entities.Employee;
import com.empman.EmpMan.repository.EmployeeRepository;
import com.empman.EmpMan.service.PayslipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payslip")
public class PayslipController {

    @Autowired
    private PayslipService payslipService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/download/{employeeId}")
    public ResponseEntity<byte[]> downloadPayslip(@PathVariable Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

//        if (employee.getAdmin() != null) {  // If the user is an admin
//            return ResponseEntity.status(403).body(null);
//        }

        byte[] pdfBytes = payslipService.generatePayslipPdf(employeeId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payslip.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}
