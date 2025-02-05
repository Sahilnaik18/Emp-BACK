package com.empman.EmpMan.service;

import com.empman.EmpMan.Entities.Employee;
import com.empman.EmpMan.repository.EmployeeRepository;
import com.empman.EmpMan.security.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmailService emailService;

    //private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Password encoder
//
//    public Employee registerEmployee(Employee employee) {
//        if (employee.getFullName() == null || employee.getEmail() == null || employee.getPassword() == null) {
//            throw new IllegalArgumentException("Full name, email, and password are required");
//        }

//        // Hash the password before saving
//        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
//
//        // Save the employee
//        Employee savedEmployee = employeeRepository.save(employee);
//
//        // Send email with login details (DO NOT send password in plain text)
//        try {
//            String subject = "Welcome to the Company - Your Login Details";
//            String body = "Hello " + employee.getFullName() + ",\n\n" +
//                    "Your account has been created successfully.\n" +
//                    "Email: " + employee.getEmail() + "\n" +
//                    "Please reset your password after logging in.\n\n" +
//                    "Best Regards,\nYour Company Team";
//
//            emailService.sendEmail(employee.getEmail(), subject, body);
//            System.out.println("✅ Email sent successfully to " + employee.getEmail());
//        } catch (Exception e) {
//            System.out.println("❌ Failed to send email: " + e.getMessage());
//        }
//
//        return savedEmployee;
//    }

//    public Employee registerEmployee(Employee employee) {
//        if (employee.getFullName() == null || employee.getEmail() == null || employee.getPassword() == null) {
//            throw new IllegalArgumentException("Full name, email, and password are required");
//        }
//
//        try {
//            // Encrypt password
//            String encryptedPassword = AESUtil.encrypt(employee.getPassword());
//            employee.setPassword(encryptedPassword);
//        } catch (Exception e) {
//            throw new RuntimeException("Error encrypting password", e);
//        }
//
//        Employee savedEmployee = employeeRepository.save(employee);
//
//        // Send email (DO NOT send password in plain text)
//        try {
//            String subject = "Welcome to the Company - Your Login Details";
//            String body = "Hello " + employee.getFullName() + ",\n\n" +
//                    "Your account has been created.\nEmail: " + employee.getEmail() + "\n" +
//                    "Please change your password after logging in.\n\nBest Regards,\nYour Company Team";
//
//            emailService.sendEmail(employee.getEmail(), subject, body);
//            System.out.println("✅ Email sent successfully to " + employee.getEmail());
//        } catch (Exception e) {
//            System.out.println("❌ Failed to send email: " + e.getMessage());
//        }
//
//        return savedEmployee;
//    }


    public Employee registerEmployee(Employee employee) {
        if (employee.getFullName() == null || employee.getEmail() == null || employee.getPassword() == null) {
            throw new IllegalArgumentException("Full name, email, and password are required");
        }

        // Store the original password before encryption
        String originalPassword = employee.getPassword();

        try {
            // Encrypt the password using AES
            String encryptedPassword = AESUtil.encrypt(originalPassword);
            employee.setPassword(encryptedPassword);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password", e);
        }

        // Save the employee
        Employee savedEmployee = employeeRepository.save(employee);

        // Send email with login details
        try {
            String subject = "Welcome to the Company - Your Login Details";
            String body = "Hello " + employee.getFullName() + ",\n\n" +
                    "Your login details are as follows:\n" +
                    "Email: " + employee.getEmail() + "\n" +
                    "Password: " + originalPassword + "\n\n" +  // Sending the original password
                    "Please change your password after logging in.\n\n" +
                    "Best Regards,\nYour Company Team";

            emailService.sendEmail(employee.getEmail(), subject, body);
            System.out.println("✅ Email sent successfully to " + employee.getEmail());
        } catch (Exception e) {
            System.out.println("❌ Failed to send email: " + e.getMessage());
        }

        return savedEmployee;
    }


//public Employee registerEmployee(Employee employee) {
//    if (employee.getFullName() == null || employee.getEmail() == null || employee.getPassword() == null) {
//        throw new IllegalArgumentException("Full name, email, and password are required");
//    }
//
//    // Save the employee first
//    Employee savedEmployee = employeeRepository.save(employee);
//
//    // Send email with login details
//    try {
//        String subject = "Welcome to the Company - Your Login Details";
//        String body = "Hello " + employee.getFullName() + ",\n\n" +
//                "Your login details are as follows:\n" +
//                "Email: " + employee.getEmail() + "\n" +
//                "Password: " + employee.getPassword() + "\n\n" +
//                "Please change your password after logging in.\n\n" +
//                "Best Regards,\nYour Company Team";
//
//        emailService.sendEmail(employee.getEmail(), subject, body);
//        System.out.println("✅ Email sent successfully to " + employee.getEmail());
//    } catch (Exception e) {
//        System.out.println("❌ Failed to send email: " + e.getMessage());
//    }
//
//    return savedEmployee;
//}

//    public Employee validateEmployee(String email, String rawPassword) {
//        Employee employee = employeeRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
//
//        // Compare raw password with hashed password
//        if (!passwordEncoder.matches(rawPassword, employee.getPassword())) {
//            throw new RuntimeException("Invalid email or password");
//        }
//
//        return employee;
//    }

    public List<Employee> getEmployeesByAdmin(Long adminId) {
        return employeeRepository.findByAdminId(adminId);
    }

    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        existingEmployee.setFullName(updatedEmployee.getFullName());
        existingEmployee.setMobile(updatedEmployee.getMobile());
        existingEmployee.setAddress(updatedEmployee.getAddress());

        return employeeRepository.save(existingEmployee);
    }

//    public Employee validateEmployee(String email, String password) {
//        return employeeRepository.findByEmailAndPassword(email, password)
//                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
//    }
public Employee validateEmployee(String email, String rawPassword) {
    Employee employee = employeeRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));

    try {
        // Decrypt stored password and log it
        String decryptedPassword = AESUtil.decrypt(employee.getPassword());
        System.out.println("Decrypted password: " + decryptedPassword); // Debugging log

        // Compare with entered password
        if (!decryptedPassword.equals(rawPassword)) {
            throw new RuntimeException("Invalid email or password");
        }
    } catch (Exception e) {
        e.printStackTrace();  // Log the exact error
        throw new RuntimeException("Error decrypting password", e);
    }

    return employee;
}


    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeRepository.delete(employee);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }
}
