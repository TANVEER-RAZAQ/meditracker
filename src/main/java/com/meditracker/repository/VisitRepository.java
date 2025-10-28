package com.meditracker.repository;

import com.meditracker.domain.Patient;
import com.meditracker.domain.Visit;
import com.meditracker.domain.enums.Department;
import com.meditracker.domain.enums.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByStatus(VisitStatus status);
    List<Visit> findByPatientOrderByCreatedAtDesc(Patient patient);
    Optional<Visit> findFirstByPatientAndStatusNotOrderByCreatedAtDesc(Patient patient, VisitStatus status);
    List<Visit> findByDepartmentAndStatusNot(Department department, VisitStatus status);
}