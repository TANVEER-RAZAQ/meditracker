package com.meditracker.controller;

import com.meditracker.controller.dto.DoctorRequest;
import com.meditracker.domain.Doctor;
import com.meditracker.domain.enums.Department;
import com.meditracker.repository.DoctorRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<Doctor>> getDoctorsByDepartment(@PathVariable Department department) {
        List<Doctor> doctors = doctorRepository.findByDepartment(department);
        return ResponseEntity.ok(doctors);
    }

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@Valid @RequestBody DoctorRequest request) {
        Doctor doctor = new Doctor();
        doctor.setFullName(request.getFullName());
        doctor.setDepartment(request.getDepartment());
        doctor.setRoomNumber(request.getRoomNumber());
        doctor.setFloor(request.getFloor());
        doctor.setConsultationFee(request.getConsultationFee() != null ? 
                request.getConsultationFee() : new BigDecimal("300.00"));
        
        Doctor saved = doctorRepository.save(doctor);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, 
                                               @Valid @RequestBody DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        
        doctor.setFullName(request.getFullName());
        doctor.setDepartment(request.getDepartment());
        doctor.setRoomNumber(request.getRoomNumber());
        doctor.setFloor(request.getFloor());
        if (request.getConsultationFee() != null) {
            doctor.setConsultationFee(request.getConsultationFee());
        }
        
        Doctor updated = doctorRepository.save(doctor);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new IllegalArgumentException("Doctor not found");
        }
        doctorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

