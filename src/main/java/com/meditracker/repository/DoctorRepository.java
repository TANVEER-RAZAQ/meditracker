package com.meditracker.repository;

import com.meditracker.domain.Doctor;
import com.meditracker.domain.enums.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findFirstByDepartment(Department department);
    List<Doctor> findByDepartment(Department department);
}