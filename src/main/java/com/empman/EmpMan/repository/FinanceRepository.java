package com.empman.EmpMan.repository;

import com.empman.EmpMan.Entities.Finance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Long> {
    List<Finance> findByEmployeeId(Long employeeId);

    List<Finance> findByAdminId(Long adminId);

    @Query(value = "SELECT * FROM finance WHERE employee_id = :employeeId AND date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)", nativeQuery = true)
    List<Finance> findLastSixMonthsByEmployee(Long employeeId);

}
