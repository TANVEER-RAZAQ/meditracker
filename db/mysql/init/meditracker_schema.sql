-- Create DB (Docker MySQL will already create it from env; this is safe idempotent)
CREATE DATABASE IF NOT EXISTS meditracker
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE meditracker;

-- Clean existing objects if re-running
SET FOREIGN_KEY_CHECKS = 0;
DROP VIEW IF EXISTS v_billing_summary_per_visit;
DROP TABLE IF EXISTS wallet_transactions;
DROP TABLE IF EXISTS billing;
DROP TABLE IF EXISTS lab_tests;
DROP TABLE IF EXISTS visits;
DROP TABLE IF EXISTS wallets;
DROP TABLE IF EXISTS doctors;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

-- Patients
CREATE TABLE patients (
                          id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                          full_name     VARCHAR(120) NOT NULL,
                          date_of_birth DATE NULL,
                          phone_number  VARCHAR(20),
                          rfid_uid      VARCHAR(64) NOT NULL UNIQUE,
                          created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;
CREATE INDEX idx_patients_rfid ON patients (rfid_uid);

-- Doctors
CREATE TABLE doctors (
                         id                BIGINT PRIMARY KEY AUTO_INCREMENT,
                         full_name         VARCHAR(120) NOT NULL,
                         department        VARCHAR(40)  NOT NULL,
                         room_number       VARCHAR(20),
                         floor             VARCHAR(10),
                         consultation_fee  DECIMAL(12,2) NOT NULL DEFAULT 300.00,
                         created_at        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         CONSTRAINT chk_doctor_department
                             CHECK (department IN ('CARDIOLOGY','GENERAL_MEDICINE','NEUROLOGY','ORTHOPEDICS','PEDIATRICS','DERMATOLOGY'))
) ENGINE=InnoDB;
CREATE INDEX idx_doctors_department ON doctors (department);

-- Wallets (1:1 with patient)
CREATE TABLE wallets (
                         id          BIGINT PRIMARY KEY AUTO_INCREMENT,
                         patient_id  BIGINT NOT NULL UNIQUE,
                         balance     DECIMAL(14,2) NOT NULL DEFAULT 0.00,
                         active      BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         CONSTRAINT fk_wallet_patient
                             FOREIGN KEY (patient_id) REFERENCES patients(id)
                                 ON DELETE CASCADE
) ENGINE=InnoDB;

-- Visits
CREATE TABLE visits (
                        id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
                        patient_id           BIGINT NOT NULL,
                        doctor_id            BIGINT NOT NULL,
                        department           VARCHAR(40) NOT NULL,
                        status               VARCHAR(40) NOT NULL DEFAULT 'REGISTERED',
                        temperature_celsius  DOUBLE,
                        bp_systolic          INT,
                        bp_diastolic         INT,
                        heart_rate           INT,
                        diagnosis            VARCHAR(1024),
                        medications          VARCHAR(1024),
                        created_at           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        CONSTRAINT fk_visit_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
                        CONSTRAINT fk_visit_doctor  FOREIGN KEY (doctor_id)  REFERENCES doctors(id),
                        CONSTRAINT chk_visit_department
                            CHECK (department IN ('CARDIOLOGY','GENERAL_MEDICINE','NEUROLOGY','ORTHOPEDICS','PEDIATRICS','DERMATOLOGY')),
                        CONSTRAINT chk_visit_status
                            CHECK (status IN ('REGISTERED','VITALS','CONSULTATION','LAB_PENDING','LAB_IN_PROGRESS','LAB_COMPLETED','BILLING_PENDING','COMPLETED'))
) ENGINE=InnoDB;
CREATE INDEX idx_visits_patient ON visits (patient_id);
CREATE INDEX idx_visits_doctor  ON visits (doctor_id);
CREATE INDEX idx_visits_status  ON visits (status);

-- Lab tests
CREATE TABLE lab_tests (
                           id           BIGINT PRIMARY KEY AUTO_INCREMENT,
                           visit_id     BIGINT NOT NULL,
                           test_name    VARCHAR(120) NOT NULL,
                           status       VARCHAR(40) NOT NULL DEFAULT 'ORDERED',
                           price        DECIMAL(12,2) NOT NULL DEFAULT 0.00,
                           result_text  VARCHAR(2048),
                           completed_at DATETIME NULL,
                           created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           CONSTRAINT fk_labtest_visit FOREIGN KEY (visit_id) REFERENCES visits(id) ON DELETE CASCADE,
                           CONSTRAINT chk_labtest_status CHECK (status IN ('ORDERED','IN_PROGRESS','COMPLETED'))
) ENGINE=InnoDB;
CREATE INDEX idx_labtests_visit  ON lab_tests (visit_id);
CREATE INDEX idx_labtests_status ON lab_tests (status);

-- Billing
CREATE TABLE billing (
                         id               BIGINT PRIMARY KEY AUTO_INCREMENT,
                         visit_id         BIGINT NOT NULL,
                         type             VARCHAR(40) NOT NULL,
                         item_description VARCHAR(160) NOT NULL,
                         amount           DECIMAL(12,2) NOT NULL DEFAULT 0.00,
                         status           VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                         paid_at          DATETIME NULL,
                         created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         CONSTRAINT fk_billing_visit FOREIGN KEY (visit_id) REFERENCES visits(id) ON DELETE CASCADE,
                         CONSTRAINT chk_billing_type   CHECK (type IN ('CONSULTATION','LAB_TEST')),
                         CONSTRAINT chk_billing_status CHECK (status IN ('PENDING','PAID'))
) ENGINE=InnoDB;
CREATE INDEX idx_billing_visit  ON billing (visit_id);
CREATE INDEX idx_billing_status ON billing (status);

-- Wallet transactions (audit)
CREATE TABLE wallet_transactions (
                                     id          BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     wallet_id   BIGINT NOT NULL,
                                     billing_id  BIGINT NULL,
                                     txn_type    VARCHAR(10) NOT NULL, -- 'DEBIT' or 'CREDIT'
                                     amount      DECIMAL(12,2) NOT NULL,
                                     description VARCHAR(255),
                                     created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     CONSTRAINT fk_wtxn_wallet  FOREIGN KEY (wallet_id)  REFERENCES wallets(id)  ON DELETE CASCADE,
                                     CONSTRAINT fk_wtxn_billing FOREIGN KEY (billing_id) REFERENCES billing(id)   ON DELETE SET NULL,
                                     CONSTRAINT chk_wtxn_type CHECK (txn_type IN ('DEBIT','CREDIT'))
) ENGINE=InnoDB;
CREATE INDEX idx_wtxn_wallet  ON wallet_transactions (wallet_id);
CREATE INDEX idx_wtxn_billing ON wallet_transactions (billing_id);

-- Summary view
CREATE VIEW v_billing_summary_per_visit AS
SELECT
    v.id AS visit_id,
    v.patient_id,
    v.doctor_id,
    COUNT(b.id) AS total_items,
    SUM(CASE WHEN b.status='PAID' THEN 1 ELSE 0 END) AS paid_items,
    SUM(CASE WHEN b.status='PENDING' THEN 1 ELSE 0 END) AS pending_items,
    COALESCE(SUM(b.amount),0) AS total_amount,
    COALESCE(SUM(CASE WHEN b.status='PAID' THEN b.amount END),0) AS total_paid,
    COALESCE(SUM(CASE WHEN b.status='PENDING' THEN b.amount END),0) AS total_due
FROM visits v
         LEFT JOIN billing b ON b.visit_id = v.id
GROUP BY v.id, v.patient_id, v.doctor_id;

-- Users (for authentication)
CREATE TABLE users (
                       id                      BIGINT PRIMARY KEY AUTO_INCREMENT,
                       username                VARCHAR(50) NOT NULL UNIQUE,
                       password                VARCHAR(255) NOT NULL,
                       full_name               VARCHAR(100) NOT NULL,
                       email                   VARCHAR(100),
                       phone_number            VARCHAR(20),
                       role                    VARCHAR(20) NOT NULL,
                       enabled                 BOOLEAN NOT NULL DEFAULT TRUE,
                       account_non_expired     BOOLEAN NOT NULL DEFAULT TRUE,
                       account_non_locked      BOOLEAN NOT NULL DEFAULT TRUE,
                       credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
                       patient_id              BIGINT NULL,
                       doctor_id               BIGINT NULL,
                       created_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       CONSTRAINT fk_user_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE SET NULL,
                       CONSTRAINT fk_user_doctor  FOREIGN KEY (doctor_id)  REFERENCES doctors(id) ON DELETE SET NULL,
                       CONSTRAINT chk_user_role CHECK (role IN ('ADMIN','DOCTOR','NURSE','LAB_TECH','BILLING','PATIENT'))
) ENGINE=InnoDB;
CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_role ON users (role);

-- Seed one doctor
INSERT INTO doctors (full_name, department, room_number, floor, consultation_fee)
VALUES ('Dr. Sahil ER.','CARDIOLOGY','205','2',300.00);