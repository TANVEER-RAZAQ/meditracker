# MediTracker - Smart Hospital Management System

A comprehensive hospital management system with RFID-based patient flow, digital wallet payments, and complete hospital workflow automation.

## Features

### Core Features
- 🏥 **Patient Registration** - RFID-based patient registration with automatic wallet creation
- 💰 **Wallet Management** - Top-up wallet balance and track transactions
- 👨‍⚕️ **Visit Management** - Complete patient journey tracking through 8 status states
- 🔬 **Lab Test Management** - Order, track, and record lab test results
- 💳 **RFID Payment System** - Digital wallet with RFID-based payments
- 📊 **Billing System** - Automated billing for consultations and lab tests
- 📱 **Notifications** - Real-time notifications for patients
- 👥 **Patient Information** - Complete patient data access and management
- 🩺 **Doctor Management** - Full CRUD operations for doctor records

### 🆕 Discharge & Summary Features
- ✅ **RFID Exit Scanning** - Patient scans RFID card at exit to complete visit
- 📋 **Comprehensive Visit Summary** - Detailed summary including vitals, diagnosis, medications, lab results, and billing
- 💌 **Discharge Notifications** - "Get well soon!" message with complete visit details
- 📚 **Visit History** - Access complete medical history anytime via API
- 💾 **Permanent Storage** - All summaries stored in database and accessible indefinitely

### 🆕 Enhanced Query Features
- 🔍 **Advanced Filtering** - Filter visits, lab tests by status
- 📋 **List Operations** - Get all patients, doctors, visits, lab tests
- 🏥 **Department Queries** - Filter doctors by department
- 🔬 **Lab Test Tracking** - Query tests by visit, status, or ID
- ⚠️ **Error Handling** - User-friendly error messages and validation

## Technology Stack

- **Backend:** Spring Boot 3.3.4
- **Language:** Java 17
- **Database:** MySQL (production) / H2 (development)
- **API Docs:** SpringDoc OpenAPI (Swagger)
- **Build Tool:** Maven
- **Containerization:** Docker & Docker Compose

## Quick Start

### Run with Docker
```bash
docker-compose up
```

### Run Locally
```bash
mvn spring-boot:run
```

### Access Application
- **API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger (✨ Interactive API Testing)
- **H2 Console:** http://localhost:8080/h2-console (dev mode)
- **Adminer:** http://localhost:8081 (MySQL management with Docker)

---

## 📚 **Documentation**

- **NEW_FEATURES_GUIDE.md** - Complete guide to all new features
- **API_QUICK_REFERENCE.md** - Quick reference for all 30 endpoints
- **TEST_NEW_FEATURES.md** - Testing instructions and examples
- **DISCHARGE_AND_SUMMARY_FEATURES.md** - Discharge workflow details
- **FINAL_SUMMARY.md** - Complete implementation summary

---

## 🚀 **API Endpoints (30 Total)**

### Registration (1)
- `POST /api/registration` - Register new patient with RFID

### Wallet Management (3) 💰
- `POST /api/wallet/topup` - **[NEW]** Add money to wallet
- `GET /api/wallet/rfid/{rfidUid}` - **[NEW]** Get wallet by RFID
- `GET /api/wallet/patient/{patientId}` - **[NEW]** Get wallet by patient ID

### Patient Management (4) 👤
- `GET /api/patients` - **[NEW]** List all patients
- `GET /api/patients/{id}` - **[NEW]** Get patient by ID
- `GET /api/patients/rfid/{rfidUid}` - **[NEW]** Get patient by RFID
- `GET /api/patients/{patientId}/visits` - **[NEW]** Get patient's visits

### Doctor Management (6) 👨‍⚕️
- `GET /api/doctors` - **[NEW]** List all doctors
- `GET /api/doctors/{id}` - **[NEW]** Get doctor by ID
- `GET /api/doctors/department/{dept}` - **[NEW]** Get doctors by department
- `POST /api/doctors` - **[NEW]** Create new doctor
- `PUT /api/doctors/{id}` - **[NEW]** Update doctor
- `DELETE /api/doctors/{id}` - **[NEW]** Delete doctor

### Visit Management (8) 🏥
- `GET /api/visits` - **[NEW]** List all visits
- `GET /api/visits/status/{status}` - **[NEW]** Filter visits by status
- `POST /api/visits/start` - Start new visit
- `POST /api/visits/{visitId}/vitals` - Record vital signs
- `POST /api/visits/{visitId}/consultation` - Add consultation notes
- `POST /api/visits/discharge` - Discharge patient with RFID scan
- `GET /api/visits/{visitId}/summary` - Get visit summary
- `GET /api/visits/history/{rfidUid}` - Get patient visit history

### Lab Tests (6) 🔬
- `GET /api/lab` - **[NEW]** List all lab tests
- `GET /api/lab/status/{status}` - **[NEW]** Filter tests by status
- `GET /api/lab/visit/{visitId}` - **[NEW]** Get tests for visit
- `GET /api/lab/{labTestId}` - **[NEW]** Get single lab test
- `POST /api/lab/{visitId}/order` - Order lab test
- `POST /api/lab/tests/{labTestId}/status` - Update lab test status

### Billing & Payment (2) 💳
- `POST /api/billing/pay` - Pay with RFID wallet
- `GET /api/billing/visit/{visitId}` - Get billing for visit

**📖 See [API_QUICK_REFERENCE.md](API_QUICK_REFERENCE.md) for complete API documentation.**

## Patient Workflow

```
Registration → Start Visit → Record Vitals → Consultation → Lab Tests (if needed)
     ↓              ↓              ↓               ↓                ↓
  RFID Scan    Assign Doctor   Nurse Station   Doctor Room     Lab Department
                                                                    ↓
← Discharge ← Payment Complete ← Lab Results Ready ← Tests Completed
     ↓
  Summary + Notification Sent
```

## Visit Status Flow

```
REGISTERED → VITALS → CONSULTATION → LAB_PENDING → LAB_IN_PROGRESS 
→ LAB_COMPLETED → BILLING_PENDING → COMPLETED
```

## Discharge Process (NEW)

When a patient scans their RFID at exit:

1. ✓ **Validates** all bills are paid
2. ✓ **Validates** all lab tests are completed
3. ✓ **Marks** visit as COMPLETED
4. ✓ **Generates** comprehensive visit summary
5. ✓ **Sends** discharge notification with:
   - Patient name and diagnosis
   - "Get well soon!" message
   - Link to complete summary
6. ✓ **Stores** summary in database (accessible anytime)

### Example Discharge Request
```bash
curl -X POST http://localhost:8080/api/visits/discharge \
  -H "Content-Type: application/json" \
  -d '{"rfidUid":"RFID_ABC123"}'
```

### Example Response (Visit Summary)
```json
{
  "visitId": 1,
  "patientName": "John Doe",
  "doctorName": "Dr. Sahil ER.",
  "department": "CARDIOLOGY",
  "diagnosis": "Mild hypertension",
  "medications": "Amlodipine 5mg once daily",
  "vitals": {
    "temperatureCelsius": 98.6,
    "bpSystolic": 120,
    "bpDiastolic": 80,
    "heartRate": 72
  },
  "billing": {
    "totalAmount": 800.00,
    "totalPaid": 800.00,
    "fullyPaid": true
  },
  "dischargedAt": "2024-10-27T15:00:00"
}
```

## Documentation

- **🔐 Security Guide:** [SECURITY_GUIDE.md](SECURITY_GUIDE.md) - Authentication, authorization, JWT tokens
- **🚪 Discharge Features:** [DISCHARGE_AND_SUMMARY_FEATURES.md](DISCHARGE_AND_SUMMARY_FEATURES.md)
- **🧪 Test Workflow:** [TEST_DISCHARGE_WORKFLOW.md](TEST_DISCHARGE_WORKFLOW.md)
- **📚 API Documentation:** http://localhost:8080/swagger
- **🗄️ Database Schema:** [db/mysql/init/meditracker_schema.sql](db/mysql/init/meditracker_schema.sql)

## Database

### Main Tables
- `patients` - Patient demographics with RFID
- `doctors` - Doctor profiles by department
- `visits` - Visit tracking with status
- `lab_tests` - Lab test orders and results
- `billing` - Itemized charges
- `wallets` - Patient digital wallets
- `wallet_transactions` - Payment audit trail

### Supported Departments
- Cardiology
- General Medicine
- Neurology
- Orthopedics
- Pediatrics
- Dermatology

## Configuration

### Profiles
- **default** - H2 in-memory database
- **mysql** - MySQL database
- **docker** - Docker environment

### Environment Variables
```bash
SPRING_PROFILES_ACTIVE=mysql
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_DATABASE=meditracker
MYSQL_USER=meditracker
MYSQL_PASSWORD=meditracker123
```

## Development

### Build
```bash
mvn clean install
```

### Run Tests
```bash
mvn test
```

### Package
```bash
mvn package
```

## Future Enhancements

- [ ] PDF summary generation
- [ ] Email/SMS integration
- [ ] Mobile app for patients
- [ ] Doctor dashboard
- [ ] Appointment scheduling
- [ ] Prescription management
- [ ] Insurance integration
- [ ] Analytics and reporting
- [ ] Multi-language support

## License

[Add your license here]

## Contributors

[Add contributors here]
