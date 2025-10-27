# 🚀 MediTracker API - Quick Reference Card

## All 30 Endpoints at a Glance

---

## 📋 **Registration**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/registration` | Register new patient with RFID |

---

## 💰 **Wallet Management**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/wallet/topup` | Add money to wallet |
| GET | `/api/wallet/rfid/{rfidUid}` | Get wallet by RFID |
| GET | `/api/wallet/patient/{patientId}` | Get wallet by patient ID |

---

## 👤 **Patient Management**

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/patients` | List all patients |
| GET | `/api/patients/{id}` | Get patient by ID |
| GET | `/api/patients/rfid/{rfidUid}` | Get patient by RFID |
| GET | `/api/patients/{patientId}/visits` | Get patient's visits |

---

## 👨‍⚕️ **Doctor Management**

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/doctors` | List all doctors |
| GET | `/api/doctors/{id}` | Get doctor by ID |
| GET | `/api/doctors/department/{dept}` | Get doctors by department |
| POST | `/api/doctors` | Create new doctor |
| PUT | `/api/doctors/{id}` | Update doctor |
| DELETE | `/api/doctors/{id}` | Delete doctor |

---

## 🏥 **Visit Management**

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/visits` | List all visits |
| GET | `/api/visits/status/{status}` | Filter visits by status |
| POST | `/api/visits/start` | Start new visit |
| POST | `/api/visits/{id}/vitals` | Record vital signs |
| POST | `/api/visits/{id}/consultation` | Add consultation |
| POST | `/api/visits/discharge` | Discharge patient |
| GET | `/api/visits/{id}/summary` | Get visit summary |
| GET | `/api/visits/history/{rfidUid}` | Get patient history |

---

## 🔬 **Lab Tests**

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/lab` | List all lab tests |
| GET | `/api/lab/status/{status}` | Filter tests by status |
| GET | `/api/lab/visit/{visitId}` | Get tests for visit |
| GET | `/api/lab/{labTestId}` | Get single lab test |
| POST | `/api/lab/{visitId}/order` | Order lab test |
| POST | `/api/lab/tests/{labTestId}/status` | Update test status |

---

## 💳 **Billing**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/billing/pay` | Pay with RFID wallet |
| GET | `/api/billing/visit/{visitId}` | Get billing for visit |

---

## 📊 **Summary by Category**

| Category | Count |
|----------|-------|
| Registration | 1 |
| Wallet | 3 |
| Patient | 4 |
| Doctor | 6 |
| Visit | 8 |
| Lab | 6 |
| Billing | 2 |
| **TOTAL** | **30** |

---

## 🎯 **Common Use Cases**

### **Patient Registration & Wallet**
```
1. POST /api/registration
2. POST /api/wallet/topup
3. GET /api/wallet/rfid/{rfidUid}
```

### **Complete Patient Visit**
```
1. POST /api/visits/start
2. POST /api/visits/{id}/vitals
3. POST /api/visits/{id}/consultation
4. POST /api/lab/{visitId}/order
5. POST /api/lab/tests/{labTestId}/status
6. POST /api/billing/pay
7. POST /api/visits/discharge
```

### **Doctor View**
```
1. GET /api/visits/status/REGISTERED
2. GET /api/patients/{id}
3. GET /api/visits/history/{rfidUid}
```

### **Lab Technician**
```
1. GET /api/lab/status/ORDERED
2. POST /api/lab/tests/{labTestId}/status
```

### **Admin Panel**
```
1. GET /api/patients
2. GET /api/doctors
3. GET /api/visits
4. POST /api/doctors
```

---

## 🔍 **Enum Values**

### **Departments**
- CARDIOLOGY
- GENERAL_MEDICINE
- NEUROLOGY
- ORTHOPEDICS
- PEDIATRICS
- DERMATOLOGY

### **Visit Status**
- REGISTERED
- VITALS
- CONSULTATION
- LAB_PENDING
- LAB_IN_PROGRESS
- LAB_COMPLETED
- BILLING_PENDING
- COMPLETED

### **Lab Test Status**
- ORDERED
- IN_PROGRESS
- COMPLETED

### **Billing Status**
- PENDING
- PAID

---

## 🌐 **Access URLs**

- **Swagger UI:** http://localhost:8080/swagger
- **API Base:** http://localhost:8080
- **Database:** http://localhost:8081

---

## 📝 **Quick Examples**

### **Top Up Wallet**
```bash
curl -X POST http://localhost:8080/api/wallet/topup \
  -H "Content-Type: application/json" \
  -d '{"rfidUid":"RFID_ABC123","amount":5000}'
```

### **Create Doctor**
```bash
curl -X POST http://localhost:8080/api/doctors \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Dr. Smith","department":"CARDIOLOGY","consultationFee":500}'
```

### **Get All Patients**
```bash
curl http://localhost:8080/api/patients
```

### **Start Visit**
```bash
curl -X POST http://localhost:8080/api/visits/start \
  -H "Content-Type: application/json" \
  -d '{"rfidUid":"RFID_ABC123","department":"CARDIOLOGY"}'
```

### **Get Pending Lab Tests**
```bash
curl http://localhost:8080/api/lab/status/ORDERED
```

---

**✅ All 30 endpoints are ready to use!**

**Open Swagger for interactive testing:** http://localhost:8080/swagger

