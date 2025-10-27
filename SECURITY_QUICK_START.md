# 🔐 Security Quick Start - 5 Minutes

## 🚀 Start the Application

```bash
cd meditracker
mvn spring-boot:run
```

**Look for these log messages:**
```
✅ Created ADMIN user - username: 'admin', password: 'admin123'
✅ Created DOCTOR user - username: 'doctor', password: 'doctor123'
✅ Created NURSE user - username: 'nurse', password: 'nurse123'
...
🔐 DEFAULT USERS CREATED - CHANGE PASSWORDS!
```

---

## 💻 Test Login (Command Line)

### 1. Login as Admin
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

**You'll get:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9BRE1JTiIsInN1YiI6ImFkbWluIiwiaWF0IjoxNzMwMDUyMDAwLCJleHAiOjE3MzAxMzg0MDB9...",
  "username": "admin",
  "fullName": "System Administrator",
  "role": "ADMIN",
  "userId": 1
}
```

### 2. Save Your Token
```bash
# Copy the token value
TOKEN="eyJhbGciOiJIUzI1NiJ9..."
```

### 3. Make Authenticated Request
```bash
curl http://localhost:8080/api/visits/history/RFID_ABC123 \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🌐 Test with Swagger UI (Browser)

### 1. Open Swagger
```
http://localhost:8080/swagger
```

### 2. Login First
1. Find `/api/auth/login` endpoint
2. Click "Try it out"
3. Enter:
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
4. Click "Execute"
5. **Copy the token** from the response

### 3. Authorize Swagger
1. Click the **"Authorize" 🔒** button at the top
2. Enter: `Bearer YOUR_TOKEN_HERE`
   - Example: `Bearer eyJhbGciOiJIUzI1NiJ9...`
3. Click "Authorize"
4. Click "Close"

### 4. Test Any Endpoint!
Now all your requests will automatically include the JWT token! ✅

---

## 🎭 Try Different Roles

### Doctor Role (Can add consultations)
```bash
# 1. Login as doctor
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"doctor","password":"doctor123"}'

# 2. Add consultation (will work ✅)
curl -X POST http://localhost:8080/api/visits/1/consultation \
  -H "Authorization: Bearer DOCTOR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "diagnosis": "Common cold",
    "medications": "Rest and fluids",
    "testsNeeded": false
  }'
```

### Nurse Role (Can record vitals)
```bash
# 1. Login as nurse
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"nurse","password":"nurse123"}'

# 2. Record vitals (will work ✅)
curl -X POST http://localhost:8080/api/visits/1/vitals \
  -H "Authorization: Bearer NURSE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "temperatureCelsius": 98.6,
    "bpSystolic": 120,
    "bpDiastolic": 80,
    "heartRate": 72
  }'
```

### Lab Tech Role (Can update lab results)
```bash
# 1. Login as lab tech
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"labtech","password":"lab123"}'

# 2. Update lab test (will work ✅)
curl -X PUT http://localhost:8080/api/lab/1/status \
  -H "Authorization: Bearer LABTECH_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "COMPLETED",
    "resultText": "Normal results"
  }'
```

---

## ❌ Test Access Denial

### Try accessing doctor endpoint as nurse (will FAIL)
```bash
# Login as nurse
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"nurse","password":"nurse123"}'

# Try to add consultation (NURSE can't do this!)
curl -X POST http://localhost:8080/api/visits/1/consultation \
  -H "Authorization: Bearer NURSE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "diagnosis": "Test",
    "medications": "Test",
    "testsNeeded": false
  }'

# ❌ Response: 403 Forbidden
```

---

## 📊 Role Access Summary

| Action | ADMIN | DOCTOR | NURSE | LAB | BILLING | PATIENT |
|--------|-------|--------|-------|-----|---------|---------|
| Login | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Start Visit | ✅ | ❌ | ✅ | ❌ | ❌ | ❌ |
| Record Vitals | ✅ | ✅ | ✅ | ❌ | ❌ | ❌ |
| Add Consultation | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| Order Lab Tests | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| Update Lab Results | ✅ | ❌ | ❌ | ✅ | ❌ | ❌ |
| Process Payment | ✅ | ❌ | ❌ | ❌ | ✅ | ✅ |
| Discharge | ✅ | ❌ | ✅ | ❌ | ✅ | ❌ |
| View Records | ✅ | ✅ | ✅ | ❌ | ✅ | ✅* |

*Patients can only view their own records

---

## 🔑 Default Credentials

| User | Password | Use Case |
|------|----------|----------|
| `admin` | `admin123` | Full system access |
| `doctor` | `doctor123` | Medical consultations |
| `nurse` | `nurse123` | Patient care |
| `labtech` | `lab123` | Lab operations |
| `billing` | `billing123` | Billing operations |
| `patient` | `patient123` | View own records |

**⚠️ IMPORTANT: Change these in production!**

---

## 🎯 Common Scenarios

### Scenario 1: Complete Patient Visit (Multiple Users)

```bash
# 1. NURSE starts visit
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"nurse","password":"nurse123"}'
# Save token: NURSE_TOKEN="..."

curl -X POST http://localhost:8080/api/visits/start \
  -H "Authorization: Bearer $NURSE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"rfidUid":"RFID_TEST","department":"CARDIOLOGY"}'

# 2. NURSE records vitals
curl -X POST http://localhost:8080/api/visits/1/vitals \
  -H "Authorization: Bearer $NURSE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"temperatureCelsius":98.6,"bpSystolic":120,"bpDiastolic":80,"heartRate":72}'

# 3. DOCTOR adds consultation
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"doctor","password":"doctor123"}'
# Save token: DOCTOR_TOKEN="..."

curl -X POST http://localhost:8080/api/visits/1/consultation \
  -H "Authorization: Bearer $DOCTOR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"diagnosis":"Hypertension","medications":"Amlodipine 5mg","testsNeeded":false}'

# 4. BILLING processes payment
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"billing","password":"billing123"}'
# Save token: BILLING_TOKEN="..."

curl -X POST http://localhost:8080/api/billing/pay \
  -H "Authorization: Bearer $BILLING_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"rfidUid":"RFID_TEST","billingId":1}'

# 5. NURSE discharges patient
curl -X POST http://localhost:8080/api/visits/discharge \
  -H "Authorization: Bearer $NURSE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"rfidUid":"RFID_TEST"}'
```

---

## ✅ Verification

### Check Current User
```bash
curl http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:**
```json
{
  "id": 1,
  "username": "admin",
  "fullName": "System Administrator",
  "email": "admin@meditracker.com",
  "role": "ADMIN",
  "enabled": true
}
```

---

## 🐛 Troubleshooting

### Problem: 401 Unauthorized
**Check:**
- Token included in header?
- Token starts with "Bearer "?
- Token not expired? (24 hour expiry)

### Problem: 403 Forbidden
**Check:**
- Using correct role for endpoint?
- Token has valid role claim?

### Problem: Invalid credentials
**Check:**
- Username spelled correctly?
- Password is correct?
- User enabled?

---

## 📖 Full Documentation

For complete security documentation, see:
- **[SECURITY_GUIDE.md](SECURITY_GUIDE.md)** - Complete security documentation
- **[README.md](README.md)** - Main project README

---

**🎉 You're now ready to use the secured MediTracker API!**

