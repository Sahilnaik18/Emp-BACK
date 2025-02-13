package com.empman.EmpMan.controller;

import com.empman.EmpMan.Entities.ProfessionalDetails;
import com.empman.EmpMan.service.ProfessionalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/professional-details")
public class ProfessionalDetailsController {

    @Autowired
    private ProfessionalDetailsService professionalDetailsService;

    @PostMapping("/add")
    public ProfessionalDetails addProfessionalDetails(
            @RequestBody ProfessionalDetails details,
            @RequestParam Long adminId,
            @RequestParam Long employeeId) {
        return professionalDetailsService.addProfessionalDetails(details, adminId, employeeId);
    }

    @GetMapping("/all")
    public List<ProfessionalDetails> getAllProfessionalDetails() {
        return professionalDetailsService.getAllProfessionalDetails();
    }

    @GetMapping("/employee/{employeeId}")
    public List<ProfessionalDetails> getProfessionalDetailsByEmployee(@PathVariable Long employeeId) {
        return professionalDetailsService.getProfessionalDetailsByEmployee(employeeId);
    }

    @GetMapping("/admin/{adminId}")
    public List<ProfessionalDetails> getProfessionalDetailsByAdmin(@PathVariable Long adminId) {
        return professionalDetailsService.getProfessionalDetailsByAdmin(adminId);
    }

    // ✅ Fix Update Endpoint
    @PutMapping("/update/{employeeId}")
    public ResponseEntity<ProfessionalDetails> updateProfessionalDetails(
            @PathVariable Long employeeId,
            @RequestBody ProfessionalDetails details) {
        ProfessionalDetails updatedDetails = professionalDetailsService.updateProfessionalDetails(employeeId, details);
        return ResponseEntity.ok(updatedDetails);
    }

    // ✅ Fix Delete Endpoint
    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<String> deleteProfessionalDetails(@PathVariable Long employeeId) {
        professionalDetailsService.deleteProfessionalDetails(employeeId);
        return ResponseEntity.ok("ProfessionalDet deleted successfully");
    }
}
