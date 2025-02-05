package com.empman.EmpMan.controller;

import com.empman.EmpMan.Entities.Finance;
import com.empman.EmpMan.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/finance")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @PostMapping("/add")
    public ResponseEntity<Finance> addFinanceRecord(@RequestBody Finance finance,
                                                    @RequestParam Long adminId,
                                                    @RequestParam(required = false) Long employeeId) {
        return ResponseEntity.ok(financeService.addFinanceRecord(finance, adminId, employeeId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Finance>> getAllFinanceRecords() {
        return ResponseEntity.ok(financeService.getAllFinanceRecords());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Finance>> getFinanceByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(financeService.getFinanceByEmployee(employeeId));
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<Finance>> getFinanceByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(financeService.getFinanceByAdmin(adminId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Finance> updateFinanceRecord(@PathVariable Long id, @RequestBody Finance financeDetails) {
        return ResponseEntity.ok(financeService.updateFinanceRecord(id, financeDetails));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFinanceRecord(@PathVariable Long id) {
        financeService.deleteFinanceRecord(id);
        return ResponseEntity.noContent().build();
    }


}
