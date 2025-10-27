package com.meditracker.repository;

import com.meditracker.domain.Wallet;
import com.meditracker.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByPatient(Patient patient);
}