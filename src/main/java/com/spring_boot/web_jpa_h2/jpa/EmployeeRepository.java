package com.spring_boot.web_jpa_h2.jpa;

import com.spring_boot.web_jpa_h2.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
