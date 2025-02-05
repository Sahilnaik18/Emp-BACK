package com.empman.EmpMan.service;

//import com.empman.EmpMan.Entities.Admin;
//import com.empman.EmpMan.repository.AdminRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class AdminService {
//    @Autowired
//    private AdminRepository adminRepository;
//
//    // Register Admin (no password hashing)
//    public Admin registerAdmin(Admin admin) {
//        // Remove password encoding as we're using plain text
//        return adminRepository.save(admin);
//    }
//
//    // Find Admin by Email
//    public Optional<Admin> findAdminByEmail(String email) {
//        return adminRepository.findByEmail(email);
//    }
//
//    // Authenticate Admin Login (without password encoding)
//    public boolean authenticateAdmin(String email, String rawPassword) {
//        Admin admin = adminRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Admin not found"));
//
//        // Compare raw password directly without hashing
//        return rawPassword.equals(admin.getPassword());
//    }
//}

import com.empman.EmpMan.Entities.Admin;
import com.empman.EmpMan.repository.AdminRepository;
import com.empman.EmpMan.util.AESUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    // Register Admin (with password encryption)
    public Admin registerAdmin(Admin admin) {
        try {
            String encryptedPassword = AESUtils.encryptPassword(admin.getPassword());  // Encrypt password
            admin.setPassword(encryptedPassword);  // Set encrypted password before saving
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password", e);
        }
        return adminRepository.save(admin);
    }

    // Find Admin by Email
    public Optional<Admin> findAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    // Authenticate Admin Login (Decrypt password during login)
    public boolean authenticateAdmin(String email, String rawPassword) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        try {
            // Decrypt the stored password
            String decryptedPassword = AESUtils.decryptPassword(admin.getPassword());
            // Compare decrypted password with the raw password entered by user
            return decryptedPassword.equals(rawPassword);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting password", e);
        }
    }
}

