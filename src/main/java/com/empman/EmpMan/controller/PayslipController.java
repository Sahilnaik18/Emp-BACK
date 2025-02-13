package com.empman.EmpMan.controller;

import com.empman.EmpMan.Entities.Employee;
import com.empman.EmpMan.repository.EmployeeRepository;
import com.empman.EmpMan.service.PayslipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
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

        byte[] pdfBytes = payslipService.generatePayslipPdf(employee);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payslip_" + employeeId + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
