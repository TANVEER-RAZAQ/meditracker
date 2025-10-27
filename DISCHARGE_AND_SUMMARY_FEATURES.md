# Patient Discharge & Visit Summary Features

## Overview
This document describes the newly implemented patient discharge and visit summary features that allow patients to:
1. Scan their RFID card at exit to complete their visit
2. Receive a comprehensive visit summary
3. Get a "Get well soon!" notification
4. Access their complete visit history anytime

---

## New API Endpoints

### 1. **Discharge Patient (RFID Scan at Exit)**

**Endpoint:** `POST /api/visits/discharge`

**Description:** When a patient scans their RFID card at the exit, this endpoint:
- Validates all bills are paid
- Validates all lab tests are completed
- Marks the visit as COMPLETED
- Generates a comprehensive visit summary
- Sends a discharge notification with "Get well soon!" message

**Request Body:**
```json
{
  "rfidUid": "RFID_ABC123"
}
```

**Response:** Returns complete `VisitSummaryDTO` (see below)

**Error Cases:**
- Patient not found with RFID
- No active visit found
- Unpaid bills exist
- Lab tests not completed

**Example cURL:**
```bash
curl -X POST http://localhost:8080/api/visits/discharge \
  -H "Content-Type: application/json" \
  -d '{"rfidUid":"RFID_ABC123"}'
```

---

### 2. **Get Visit Summary**

**Endpoint:** `GET /api/visits/{visitId}/summary`

**Description:** Retrieve a complete summary of any visit by its ID.

**Response Example:**
```json
{
  "visitId": 1,
  "visitDate": "2024-10-27T10:30:00",
  "status": "COMPLETED",
  "patientName": "John Doe",
  "patientPhone": "+1234567890",
  "rfidUid": "RFID_ABC123",
  "doctorName": "Dr. Sahil ER.",
  "department": "CARDIOLOGY",
  "roomNumber": "205",
  "vitals": {
    "temperatureCelsius": 98.6,
    "bpSystolic": 120,
    "bpDiastolic": 80,
    "heartRate": 72
  },
  "diagnosis": "Mild hypertension",
  "medications": "Amlodipine 5mg once daily",
  "labTests": [
    {
      "id": 1,
      "testName": "Complete Blood Count",
      "status": "COMPLETED",
      "price": 500.00,
      "resultText": "All values within normal range",
      "completedAt": "2024-10-27T14:00:00"
    }
  ],
  "billing": {
    "items": [
      {
        "id": 1,
        "type": "CONSULTATION",
        "description": "Consultation - Dr. Sahil ER.",
        "amount": 300.00,
        "status": "PAID",
        "paidAt": "2024-10-27T11:00:00"
      },
      {
        "id": 2,
        "type": "LAB_TEST",
        "description": "Complete Blood Count",
        "amount": 500.00,
        "status": "PAID",
        "paidAt": "2024-10-27T14:30:00"
      }
    ],
    "totalAmount": 800.00,
    "totalPaid": 800.00,
    "totalDue": 0.00,
    "fullyPaid": true
  },
  "createdAt": "2024-10-27T10:30:00",
  "updatedAt": "2024-10-27T15:00:00",
  "dischargedAt": "2024-10-27T15:00:00"
}
```

---

### 3. **Get Patient Visit History**

**Endpoint:** `GET /api/visits/history/{rfidUid}`

**Description:** Retrieve complete visit history for a patient using their RFID. Returns all past visits with summaries, ordered by most recent first.

**Path Parameter:** `rfidUid` - Patient's RFID card UID

**Response:** Array of `VisitSummaryDTO` objects

**Example:**
```bash
curl http://localhost:8080/api/visits/history/RFID_ABC123
```

**Use Cases:**
- Patient portal - view medical history
- Doctor consultation - review past visits
- Billing department - check payment history
- Insurance claims - provide visit documentation

---

## Discharge Notification

When a patient is discharged, they receive a comprehensive notification:

**Notification Title:** "Discharge Summary - Visit Completed"

**Notification Body:**
```
Dear [Patient Name],

Your visit has been successfully completed.
Diagnosis: [Diagnosis if available]

Please follow your doctor's advice and take prescribed medications regularly.
Your complete visit summary is available in your patient portal.

🌟 Get well soon! 🌟

Thank you for choosing our healthcare services.
We wish you a speedy recovery!
```

### Integration Points
The notification is logged and ready for integration with:
- **Firebase Cloud Messaging** - Push notifications to mobile app
- **SMS Gateway** - Text message notifications
- **Email Service** - Detailed email summaries with PDF attachments

---

## Visit Summary Data Structure

### VisitSummaryDTO Components

1. **Basic Visit Info**
   - Visit ID
   - Visit date/time
   - Current status
   - Created/Updated/Discharged timestamps

2. **Patient Information**
   - Full name
   - Phone number
   - RFID card UID

3. **Doctor Information**
   - Doctor name
   - Department
   - Room number

4. **Vitals Information**
   - Temperature (Celsius)
   - Blood pressure (Systolic/Diastolic)
   - Heart rate

5. **Consultation Details**
   - Diagnosis
   - Prescribed medications

6. **Lab Tests** (if any)
   - Test name
   - Status (ORDERED/IN_PROGRESS/COMPLETED)
   - Price
   - Results
   - Completion timestamp

7. **Billing Summary**
   - Individual billing items
   - Total amount
   - Total paid
   - Total due
   - Payment status (fully paid or not)

---

## Workflow: Patient Exit Process

```
1. Patient completes all treatments/consultations
2. Patient pays all pending bills (RFID wallet or other methods)
3. Lab tests completed (if any were ordered)
4. Patient scans RFID card at exit station
   ↓
5. System validates:
   ✓ All bills paid
   ✓ All lab tests completed
   ↓
6. Visit marked as COMPLETED
   ↓
7. Comprehensive summary generated
   ↓
8. Discharge notification sent with "Get well soon!" message
   ↓
9. Summary stored in database (accessible anytime via history API)
   ↓
10. Patient receives notification on mobile/SMS/email
```

---

## Database Storage

All visit summaries are **automatically stored** and accessible through:
- The Visit entity (with all related entities: Patient, Doctor, Billing, LabTests)
- Accessed via the history endpoint at any time
- No expiration - complete medical history maintained
- Searchable and retrievable by RFID

The data is stored in a normalized relational structure:
- `visits` table - core visit information
- `patients` table - patient demographics
- `doctors` table - doctor information
- `billing` table - itemized charges
- `lab_tests` table - test orders and results

---

## Security Considerations

1. **RFID Validation**: Only valid registered RFID cards can trigger discharge
2. **Business Rules**: System enforces payment and lab completion before discharge
3. **Audit Trail**: All timestamps recorded (created, updated, discharged)
4. **Data Privacy**: Patient data protected through proper access controls (implement authentication/authorization as needed)

---

## Example Usage Scenarios

### Scenario 1: Normal Discharge Flow
```bash
# 1. Patient completes visit, all bills paid, no labs
POST /api/visits/discharge
{
  "rfidUid": "RFID_ABC123"
}

# Response: Complete visit summary + notification sent
```

### Scenario 2: Retrieve Past Visit
```bash
# Patient wants to see their last visit details
GET /api/visits/history/RFID_ABC123

# Response: Array of all visits, most recent first
```

### Scenario 3: Doctor Reviews Patient History
```bash
# During consultation, doctor retrieves patient's medical history
GET /api/visits/history/RFID_ABC123

# Response: Complete history with diagnoses, medications, lab results
```

---

## Future Enhancements

Potential improvements for these features:

1. **PDF Generation**: Generate downloadable PDF summaries
2. **Email Integration**: Automatically email summary to patient
3. **SMS Notifications**: Send summary link via SMS
4. **Multi-language Support**: Notifications in patient's preferred language
5. **Prescription Printing**: Print prescription at discharge
6. **Follow-up Scheduling**: Suggest follow-up appointment dates
7. **Patient Satisfaction Survey**: Trigger feedback request after discharge
8. **Insurance Integration**: Auto-submit claims with summary
9. **Pharmacy Integration**: Send prescriptions to selected pharmacy
10. **Mobile App Deep Links**: Direct link to view summary in app

---

## Testing

### Test Discharge Flow
```bash
# 1. Register patient
POST /api/registration
{
  "fullName": "Test Patient",
  "phoneNumber": "1234567890",
  "rfidUid": "TEST_RFID_001"
}

# 2. Start visit
POST /api/visits/start
{
  "rfidUid": "TEST_RFID_001",
  "department": "GENERAL_MEDICINE"
}

# 3. Record vitals
POST /api/visits/{visitId}/vitals
{
  "temperatureCelsius": 98.6,
  "bpSystolic": 120,
  "bpDiastolic": 80,
  "heartRate": 72
}

# 4. Add consultation
POST /api/visits/{visitId}/consultation
{
  "diagnosis": "Common cold",
  "medications": "Rest and fluids",
  "testsNeeded": false
}

# 5. Pay bills (using RFID wallet)
POST /api/billing/pay
{
  "rfidUid": "TEST_RFID_001",
  "billingId": {billingId}
}

# 6. Discharge patient
POST /api/visits/discharge
{
  "rfidUid": "TEST_RFID_001"
}

# 7. Verify history
GET /api/visits/history/TEST_RFID_001
```

---

## Support

For questions or issues with these features, please refer to:
- API Documentation: http://localhost:8080/swagger
- Application logs for notification delivery status
- Database audit tables for transaction history

