package com.meditracker.repository;


import com.meditracker.domain.LabTest;
import com.meditracker.domain.Visit;
import com.meditracker.domain.enums.LabTestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LabTestRepository extends JpaRepository<LabTest, Long> {
    List<LabTest> findByVisit(Visit visit);
    List<LabTest> findByStatus(LabTestStatus status);
}