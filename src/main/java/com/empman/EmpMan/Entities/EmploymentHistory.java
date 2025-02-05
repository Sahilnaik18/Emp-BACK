package com.empman.EmpMan.Entities;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class EmploymentHistory {
    private String companyName;
    private LocalDate joiningDate;
    private LocalDate endDate;

    // Getters and Setters

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
