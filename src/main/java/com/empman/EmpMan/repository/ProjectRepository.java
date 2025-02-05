package com.empman.EmpMan.repository;

import com.empman.EmpMan.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByEmployeeId(Long employeeId);
    List<Project> findByAdminId(Long adminId);
}
