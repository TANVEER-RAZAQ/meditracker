# Test: Complete Patient Visit with Discharge

This guide demonstrates a complete patient visit workflow from registration to discharge.

## Prerequisites
- Application running on http://localhost:8080
- REST client (curl, Postman, or similar)

## Complete Workflow Test

### Step 1: Register a New Patient
```bash
curl -X POST http://localhost:8080/api/registration \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Alice Johnson",
    "dateOfBirth": "1990-05-15",
    "phoneNumber": "+1-555-0123",
    "rfidUid": "RFID_ALICE_001"
  }'
```

**Expected Response:**
```json
{
  "id": 1,
  "fullName": "Alice Johnson",
  "dateOfBirth": "1990-05-15",
  "phoneNumber": "+1-555-0123",
  "rfidUid": "RFID_ALICE_001",
  "walletBalance": 5000.00
}
```

---

### Step 2: Start a Visit (Patient Scans RFID at Reception)
```bash
curl -X POST http://localhost:8080/api/visits/start \
  -H "Content-Type: application/json" \
  -d '{
    "rfidUid": "RFID_ALICE_001",
    "department": "CARDIOLOGY"
  }'
```

**Expected Response:**
```json
{
  "id": 1,
  "patient": {...},
  "doctor": {
    "id": 1,
    "fullName": "Dr. Sahil ER.",
    "department": "CARDIOLOGY"
  },
  "status": "REGISTERED",
  "createdAt": "2024-10-27T10:00:00"
}
```

**Note the visit ID** for subsequent steps.

---

### Step 3: Record Vitals (Nurse Station)
```bash
curl -X POST http://localhost:8080/api/visits/1/vitals \
  -H "Content-Type: application/json" \
  -d '{
    "temperatureCelsius": 98.6,
    "bpSystolic": 135,
    "bpDiastolic": 85,
    "heartRate": 78
  }'
```

**Expected Response:**
```json
{
  "id": 1,
  "status": "VITALS",
  "temperatureCelsius": 98.6,
  "bpSystolic": 135,
  "bpDiastolic": 85,
  "heartRate": 78
}
```

---

### Step 4: Doctor Consultation
```bash
curl -X POST http://localhost:8080/api/visits/1/consultation \
  -H "Content-Type: application/json" \
  -d '{
    "diagnosis": "Stage 1 Hypertension",
    "medications": "Amlodipine 5mg once daily, Regular exercise, Low sodium diet",
    "testsNeeded": true
  }'
```

**Expected Response:**
```json
{
  "id": 1,
  "status": "LAB_PENDING",
  "diagnosis": "Stage 1 Hypertension",
  "medications": "Amlodipine 5mg once daily, Regular exercise, Low sodium diet"
}
```

---

### Step 5: Order Lab Tests
```bash
curl -X POST http://localhost:8080/api/lab/order \
  -H "Content-Type: application/json" \
  -d '{
    "visitId": 1,
    "testName": "Lipid Profile",
    "price": 800.00
  }'
```

**Expected Response:**
```json
{
  "id": 1,
  "testName": "Lipid Profile",
  "status": "ORDERED",
  "price": 800.00
}
```

---

### Step 6: Complete Lab Test
```bash
# Update status to IN_PROGRESS
curl -X PUT http://localhost:8080/api/lab/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "IN_PROGRESS"
  }'

# Complete the test with results
curl -X PUT http://localhost:8080/api/lab/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "COMPLETED",
    "resultText": "Total Cholesterol: 220 mg/dL, LDL: 140 mg/dL, HDL: 45 mg/dL, Triglycerides: 175 mg/dL"
  }'
```

---

### Step 7: Check Billing
```bash
curl http://localhost:8080/api/billing/visit/1
```

**Expected Response:**
```json
[
  {
    "id": 1,
    "type": "CONSULTATION",
    "itemDescription": "Consultation - Dr. Sahil ER.",
    "amount": 300.00,
    "status": "PENDING"
  },
  {
    "id": 2,
    "type": "LAB_TEST",
    "itemDescription": "Lipid Profile",
    "amount": 800.00,
    "status": "PENDING"
  }
]
```

**Total Due: 1,100.00**

---

### Step 8: Pay Bills (RFID Wallet Payment)
```bash
# Pay consultation fee
curl -X POST http://localhost:8080/api/billing/pay \
  -H "Content-Type: application/json" \
  -d '{
    "rfidUid": "RFID_ALICE_001",
    "billingId": 1
  }'

# Pay lab test fee
curl -X POST http://localhost:8080/api/billing/pay \
  -H "Content-Type: application/json" \
  -d '{
    "rfidUid": "RFID_ALICE_001",
    "billingId": 2
  }'
```

**Expected:** Both payments successful, wallet balance reduced by 1,100.00

---

### Step 9: 🆕 DISCHARGE - Patient Scans RFID at Exit

**This is the NEW feature!**

```bash
curl -X POST http://localhost:8080/api/visits/discharge \
  -H "Content-Type: application/json" \
  -d '{
    "rfidUid": "RFID_ALICE_001"
  }'
```

**Expected Response: Complete Visit Summary**
```json
{
  "visitId": 1,
  "visitDate": "2024-10-27T10:00:00",
  "status": "COMPLETED",
  "patientName": "Alice Johnson",
  "patientPhone": "+1-555-0123",
  "rfidUid": "RFID_ALICE_001",
  "doctorName": "Dr. Sahil ER.",
  "department": "CARDIOLOGY",
  "roomNumber": "205",
  "vitals": {
    "temperatureCelsius": 98.6,
    "bpSystolic": 135,
    "bpDiastolic": 85,
    "heartRate": 78
  },
  "diagnosis": "Stage 1 Hypertension",
  "medications": "Amlodipine 5mg once daily, Regular exercise, Low sodium diet",
  "labTests": [
    {
      "id": 1,
      "testName": "Lipid Profile",
      "status": "COMPLETED",
      "price": 800.00,
      "resultText": "Total Cholesterol: 220 mg/dL, LDL: 140 mg/dL, HDL: 45 mg/dL, Triglycerides: 175 mg/dL",
      "completedAt": "2024-10-27T14:30:00"
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
        "paidAt": "2024-10-27T14:00:00"
      },
      {
        "id": 2,
        "type": "LAB_TEST",
        "description": "Lipid Profile",
        "amount": 800.00,
        "status": "PAID",
        "paidAt": "2024-10-27T14:20:00"
      }
    ],
    "totalAmount": 1100.00,
    "totalPaid": 1100.00,
    "totalDue": 0.00,
    "fullyPaid": true
  },
  "createdAt": "2024-10-27T10:00:00",
  "updatedAt": "2024-10-27T15:00:00",
  "dischargedAt": "2024-10-27T15:00:00"
}
```

**Patient Receives Notification:**
```
Dear Alice Johnson,

Your visit has been successfully completed.
Diagnosis: Stage 1 Hypertension

Please follow your doctor's advice and take prescribed medications regularly.
Your complete visit summary is available in your patient portal.

🌟 Get well soon! 🌟

Thank you for choosing our healthcare services.
We wish you a speedy recovery!
```

---

### Step 10: 🆕 View Patient History (Anytime in Future)

Patient can access their complete medical history anytime:

```bash
curl http://localhost:8080/api/visits/history/RFID_ALICE_001
```

**Expected Response:** Array of all visits with complete summaries
```json
[
  {
    "visitId": 1,
    "visitDate": "2024-10-27T10:00:00",
    "status": "COMPLETED",
    "diagnosis": "Stage 1 Hypertension",
    "medications": "Amlodipine 5mg once daily, Regular exercise, Low sodium diet",
    ...complete summary...
  },
  ...previous visits if any...
]
```

---

## Error Cases to Test

### 1. Discharge with Unpaid Bills
```bash
# Try to discharge before paying all bills
curl -X POST http://localhost:8080/api/visits/discharge \
  -H "Content-Type: application/json" \
  -d '{
    "rfidUid": "RFID_ALICE_001"
  }'
```
**Expected Error:**
```json
{
  "error": "Cannot discharge patient - unpaid bills exist"
}
```

### 2. Discharge with Incomplete Lab Tests
```bash
# Try to discharge with lab tests still ORDERED or IN_PROGRESS
```
**Expected Error:**
```json
{
  "error": "Cannot discharge patient - lab tests are not completed"
}
```

### 3. Discharge with No Active Visit
```bash
# Try to discharge a patient with no active visits
curl -X POST http://localhost:8080/api/visits/discharge \
  -H "Content-Type: application/json" \
  -d '{
    "rfidUid": "UNKNOWN_RFID"
  }'
```
**Expected Error:**
```json
{
  "error": "No active visit found for patient"
}
```

---

## Verification Checklist

After completing the workflow, verify:

- [ ] Visit status changed to COMPLETED
- [ ] Discharge notification logged (check application logs)
- [ ] Visit summary contains all details:
  - [ ] Patient information
  - [ ] Doctor information
  - [ ] Vitals recorded
  - [ ] Diagnosis and medications
  - [ ] All lab tests with results
  - [ ] Complete billing breakdown
  - [ ] All bills marked as PAID
  - [ ] Discharge timestamp recorded
- [ ] Patient history accessible via API
- [ ] Summary permanently stored in database

---

## Logs to Check

Check application logs for discharge notification:

```
[DISCHARGE] Sending discharge notification to patient 1
[DISCHARGE] Title: Discharge Summary - Visit Completed
[DISCHARGE] Body:
Dear Alice Johnson,

Your visit has been successfully completed.
Diagnosis: Stage 1 Hypertension

Please follow your doctor's advice and take prescribed medications regularly.
Your complete visit summary is available in your patient portal.

🌟 Get well soon! 🌟

Thank you for choosing our healthcare services.
We wish you a speedy recovery!
```

---

## Postman Collection

Import this collection for easier testing:

1. Visit: http://localhost:8080/swagger
2. Download OpenAPI spec
3. Import into Postman
4. Test all endpoints

---

## Notes

- **RFID_ALICE_001** is used consistently for this test patient
- Initial wallet balance is 5,000.00
- After discharge, wallet balance should be 3,900.00 (5000 - 1100)
- Summary is accessible indefinitely via history API
- Patient can have multiple visits - each gets its own summary

---

## Next Steps After Testing

1. **Integrate with mobile app** - Display summaries in patient portal
2. **Add PDF generation** - Download summary as PDF
3. **Email integration** - Auto-email summary after discharge
4. **SMS notifications** - Send discharge message via SMS
5. **Print summary** - Print at exit kiosk

