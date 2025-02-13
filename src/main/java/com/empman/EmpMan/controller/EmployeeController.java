package com.empman.EmpMan.controller;

import com.empman.EmpMan.Entities.Employee;
import com.empman.EmpMan.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


@PostMapping("/login")
public ResponseEntity<Employee> login(@RequestBody Map<String, String> credentials) {
    String email = credentials.get("email");
    String rawPassword = credentials.get("password");
    System.out.println("Raw password received: " + rawPassword); // Log raw password

    return ResponseEntity.ok(employeeService.validateEmployee(email, rawPassword));
}


    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
}
