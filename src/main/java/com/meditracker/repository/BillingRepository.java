package com.meditracker.repository;

import com.meditracker.domain.Billing;
import com.meditracker.domain.Visit;
import com.meditracker.domain.enums.BillingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BillingRepository extends JpaRepository<Billing, Long> {
    List<Billing> findByVisit(Visit visit);
    List<Billing> findByStatus(BillingStatus status);
}