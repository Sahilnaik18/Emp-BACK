package com.empman.EmpMan.Entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ProfessionalDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 6)
    private String employmentCode;


    private String companyMail;

    @Column(nullable = false, length = 12)
    private String officePhone;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String officeAddressLine1;

    private String officeAddressLine2;

    @Column(nullable = false, length = 6)
    private String pincode;

    @Column(nullable = false)
    private String reportingManager;

    @Column(nullable = false)
    private String hrName;

    @Column(nullable = false)
    private String dateOfJoining;

    @ElementCollection
    private List<EmploymentHistory> employmentHistory;

    // Foreign key linking to Employee
    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // Foreign key linking to Admin (To track which admin added this)
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmploymentCode() {
        return employmentCode;
    }

    public void setEmploymentCode(String employmentCode) {
        this.employmentCode = employmentCode;
    }

    public String getCompanyMail() {
        return companyMail;
    }

    public void setCompanyMail(String companyMail) {
        this.companyMail = companyMail;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOfficeAddressLine1() {
        return officeAddressLine1;
    }

    public void setOfficeAddressLine1(String officeAddressLine1) {
        this.officeAddressLine1 = officeAddressLine1;
    }

    public String getOfficeAddressLine2() {
        return officeAddressLine2;
    }

    public void setOfficeAddressLine2(String officeAddressLine2) {
        this.officeAddressLine2 = officeAddressLine2;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getReportingManager() {
        return reportingManager;
    }

    public void setReportingManager(String reportingManager) {
        this.reportingManager = reportingManager;
    }

    public String getHrName() {
        return hrName;
    }

    public void setHrName(String hrName) {
        this.hrName = hrName;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public List<EmploymentHistory> getEmploymentHistory() {
        return employmentHistory;
    }

    public void setEmploymentHistory(List<EmploymentHistory> employmentHistory) {
        this.employmentHistory = employmentHistory;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
