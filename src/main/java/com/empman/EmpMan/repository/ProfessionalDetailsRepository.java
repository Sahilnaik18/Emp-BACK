package com.empman.EmpMan.repository;

import com.empman.EmpMan.Entities.ProfessionalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProfessionalDetailsRepository extends JpaRepository<ProfessionalDetails, Long> {
    List<ProfessionalDetails> findByEmployeeId(Long employeeId);
    List<ProfessionalDetails> findByAdminId(Long adminId);
}
