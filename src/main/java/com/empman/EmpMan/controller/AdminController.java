package com.empman.EmpMan.controller;

import com.empman.EmpMan.Entities.Admin;
import com.empman.EmpMan.Entities.Employee;
import com.empman.EmpMan.repository.AdminRepository;
import com.empman.EmpMan.service.AdminService;
import com.empman.EmpMan.service.EmailService;
import com.empman.EmpMan.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/register")
    public ResponseEntity<Admin> registerAdmin(@RequestBody Admin admin) {
        return ResponseEntity.ok(adminService.registerAdmin(admin));
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginAdmin(@RequestParam String email, @RequestParam String password) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        boolean isAuthenticated = adminService.authenticateAdmin(email, password);

        if (isAuthenticated) {
            // Set Security Context
            User authenticatedUser = new User(email, password, new ArrayList<>());
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(authenticatedUser, password, authenticatedUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // ✅ Return admin ID and email in JSON response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("adminId", admin.getId()); // Include admin ID
            response.put("email", admin.getEmail());

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


    @PostMapping("/employee")
    public ResponseEntity<Employee> registerEmployee(@RequestBody Employee employee, @RequestParam Long adminId) {
        Optional<Admin> admin = adminRepository.findById(adminId);
        if (admin.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Assign the admin to the employee
        employee.setAdmin(admin.get());

        Employee savedEmployee = employeeService.registerEmployee(employee);

        // Send Email (Handled in Try-Catch to Avoid Crashes)
        try {
            emailService.sendEmail(employee.getEmail(), "Login Details",
                    "Your login details:\nEmail: " + employee.getEmail() + "\nPassword: " + employee.getPassword());
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }

        return ResponseEntity.ok(savedEmployee);
    }



    @GetMapping("/employees/{adminId}")
    public ResponseEntity<List<Employee>> getEmployeesByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(employeeService.getEmployeesByAdmin(adminId));
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, updatedEmployee));
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }
}
