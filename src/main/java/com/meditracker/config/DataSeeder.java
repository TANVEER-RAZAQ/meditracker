package com.meditracker.config;

import com.meditracker.domain.Doctor;
import com.meditracker.domain.enums.Department;
import com.meditracker.repository.DoctorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataSeeder {
    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Bean
    CommandLineRunner seedDoctors(DoctorRepository doctorRepository) {
        return args -> {
            if (doctorRepository.count() == 0) {
                Doctor doc = new Doctor();
                doc.setFullName("Dr. Alice Heart");
                doc.setDepartment(Department.CARDIOLOGY);
                doc.setFloor("2");
                doc.setRoomNumber("205");
                doc.setConsultationFee(new BigDecimal("300.00"));
                doctorRepository.save(doc);
                log.info("Seeded demo doctor: {}", doc.getFullName());
            }
        };
    }
}