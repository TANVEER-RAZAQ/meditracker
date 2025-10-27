# 🔐 MediTracker API Security Guide

## Overview

MediTracker API is now secured with **JWT (JSON Web Token) authentication** and **Role-Based Access Control (RBAC)**.

---

## 🎭 User Roles

The system supports **6 different roles** with specific permissions:

| Role | Description | Access Level |
|------|-------------|--------------|
| **ADMIN** | System Administrator | Full access to all endpoints |
| **DOCTOR** | Medical Doctor | Consultations, prescriptions, lab orders |
| **NURSE** | Nurse | Patient vitals, visit management, discharge |
| **LAB_TECH** | Lab Technician | Lab test result updates |
| **BILLING** | Billing Staff | Payment management, billing operations |
| **PATIENT** | Patient | View own records, payment history |

---

## 🔑 Default Users

The system creates default users on first startup:

| Username | Password | Role | Email |
|----------|----------|------|-------|
| `admin` | `admin123` | ADMIN | admin@meditracker.com |
| `doctor` | `doctor123` | DOCTOR | doctor@meditracker.com |
| `nurse` | `nurse123` | NURSE | nurse@meditracker.com |
| `labtech` | `lab123` | LAB_TECH | lab@meditracker.com |
| `billing` | `billing123` | BILLING | billing@meditracker.com |
| `patient` | `patient123` | PATIENT | patient@meditracker.com |

⚠️ **IMPORTANT:** Change these passwords immediately in production!

---

## 🚀 Getting Started

### 1. Login

**Endpoint:** `POST /api/auth/login`

**Request:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "fullName": "System Administrator",
  "role": "ADMIN",
  "userId": 1
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

---

### 2. Register New User

**Endpoint:** `POST /api/auth/register`

**Request:**
```json
{
  "username": "newdoctor",
  "password": "secure123",
  "fullName": "Dr. Sarah Williams",
  "email": "sarah@meditracker.com",
  "phoneNumber": "+1-555-0199",
  "role": "DOCTOR"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "newdoctor",
  "fullName": "Dr. Sarah Williams",
  "role": "DOCTOR",
  "userId": 7
}
```

---

### 3. Using JWT Token

Include the JWT token in the `Authorization` header for all protected endpoints:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Example cURL:**
```bash
curl http://localhost:8080/api/visits/1/summary \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

---

## 🔒 Endpoint Security

### Public Endpoints (No Authentication Required)

```
POST   /api/auth/login           - User login
POST   /api/auth/register        - User registration
POST   /api/registration         - Patient registration (RFID)
GET    /swagger-ui/**            - API documentation
GET    /h2-console/**            - H2 database console (dev only)
```

---

### Admin Only Endpoints

```
ALL    /api/admin/**             - All admin operations
```

---

### Doctor Endpoints

```
POST   /api/visits/{id}/consultation       - Add consultation notes
POST   /api/lab/order                       - Order lab tests
```
**Required Role:** DOCTOR or ADMIN

---

### Nurse Endpoints

```
POST   /api/visits/start          - Start new visit
POST   /api/visits/{id}/vitals    - Record patient vitals
POST   /api/visits/discharge      - Discharge patient
```
**Required Role:** NURSE, DOCTOR, or ADMIN

---

### Lab Technician Endpoints

```
PUT    /api/lab/{id}/status       - Update lab test status/results
```
**Required Role:** LAB_TECH or ADMIN

---

### Billing Endpoints

```
POST   /api/billing/pay           - Process payment
GET    /api/billing/**            - View billing information
```
**Required Role:** BILLING, ADMIN, or PATIENT (own records only)

---

### Patient Endpoints

```
GET    /api/visits/history/{rfidUid}    - View visit history
GET    /api/visits/{id}/summary         - View visit summary
GET    /api/billing/visit/{id}          - View billing for visit
```
**Required Role:** PATIENT, DOCTOR, NURSE, or ADMIN

---

## 💡 Usage Examples

### Example 1: Doctor Starting Work

```bash
# 1. Login as doctor
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"doctor","password":"doctor123"}'

# Response includes token
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

# 2. Add consultation notes (doctor only)
curl -X POST http://localhost:8080/api/visits/1/consultation \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "diagnosis": "Hypertension",
    "medications": "Amlodipine 5mg daily",
    "testsNeeded": true
  }'

# 3. Order lab test (doctor only)
curl -X POST http://localhost:8080/api/lab/order \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "visitId": 1,
    "testName": "Lipid Profile",
    "price": 800.00
  }'
```

---

### Example 2: Nurse Managing Patient

```bash
# 1. Login as nurse
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"nurse","password":"nurse123"}'

TOKEN="..."

# 2. Start visit
curl -X POST http://localhost:8080/api/visits/start \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "rfidUid": "RFID_ABC123",
    "department": "CARDIOLOGY"
  }'

# 3. Record vitals
curl -X POST http://localhost:8080/api/visits/1/vitals \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "temperatureCelsius": 98.6,
    "bpSystolic": 120,
    "bpDiastolic": 80,
    "heartRate": 72
  }'

# 4. Discharge patient (after all procedures complete)
curl -X POST http://localhost:8080/api/visits/discharge \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"rfidUid":"RFID_ABC123"}'
```

---

### Example 3: Lab Technician Processing Tests

```bash
# 1. Login as lab tech
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"labtech","password":"lab123"}'

TOKEN="..."

# 2. Update test status to IN_PROGRESS
curl -X PUT http://localhost:8080/api/lab/1/status \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status":"IN_PROGRESS"}'

# 3. Complete test with results
curl -X PUT http://localhost:8080/api/lab/1/status \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "COMPLETED",
    "resultText": "All values within normal range"
  }'
```

---

### Example 4: Patient Viewing Records

```bash
# 1. Login as patient
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"patient","password":"patient123"}'

TOKEN="..."

# 2. View visit history
curl http://localhost:8080/api/visits/history/RFID_PATIENT_001 \
  -H "Authorization: Bearer $TOKEN"

# 3. View specific visit summary
curl http://localhost:8080/api/visits/1/summary \
  -H "Authorization: Bearer $TOKEN"

# 4. View billing
curl http://localhost:8080/api/billing/visit/1 \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🛡️ Security Features

### JWT Token Details

- **Algorithm:** HS256 (HMAC with SHA-256)
- **Expiration:** 24 hours (configurable)
- **Claims:** username, role, issued date, expiry date
- **Storage:** Client-side (localStorage or sessionStorage)

### Password Security

- **Encoding:** BCrypt with salt
- **Minimum Length:** 6 characters (configurable)
- **Stored:** Hashed passwords only, never plain text

### Session Management

- **Stateless:** No server-side sessions
- **Token-based:** JWT tokens for authentication
- **Refresh:** Tokens expire after 24 hours, re-login required

---

## ⚙️ Configuration

### JWT Settings

Configure JWT in `application.yml` or `application.properties`:

```yaml
jwt:
  secret: "MediTrackerSecretKeyForJWTTokenGenerationAndValidationMinimum256BitsRequired"
  expiration: 86400000  # 24 hours in milliseconds
```

**⚠️ Important:** Change the secret key in production to a secure random value!

### Generate Secure Secret

```bash
# Generate a secure 256-bit secret
openssl rand -base64 32
```

---

## 🚫 Error Handling

### 401 Unauthorized

**Causes:**
- No token provided
- Invalid token
- Expired token
- Wrong credentials

**Response:**
```json
{
  "timestamp": "2024-10-27T15:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required"
}
```

### 403 Forbidden

**Causes:**
- Valid token but insufficient permissions
- Wrong role for endpoint

**Response:**
```json
{
  "timestamp": "2024-10-27T15:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied"
}
```

---

## 🧪 Testing with Swagger UI

1. Open Swagger UI: http://localhost:8080/swagger

2. Click **"Authorize"** button (🔒 icon)

3. Enter your JWT token:
   ```
   Bearer eyJhbGciOiJIUzI1NiJ9...
   ```

4. Click **"Authorize"**

5. All requests will now include the token automatically

---

## 📊 Role Permission Matrix

| Endpoint | ADMIN | DOCTOR | NURSE | LAB_TECH | BILLING | PATIENT |
|----------|-------|--------|-------|----------|---------|---------|
| Login/Register | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Start Visit | ✅ | ❌ | ✅ | ❌ | ❌ | ❌ |
| Record Vitals | ✅ | ✅ | ✅ | ❌ | ❌ | ❌ |
| Add Consultation | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| Order Lab Tests | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| Update Lab Results | ✅ | ❌ | ❌ | ✅ | ❌ | ❌ |
| Process Payment | ✅ | ❌ | ❌ | ❌ | ✅ | ✅ |
| Discharge Patient | ✅ | ❌ | ✅ | ❌ | ✅ | ❌ |
| View Patient Records | ✅ | ✅ | ✅ | ❌ | ✅ | ✅* |

*Patients can only view their own records

---

## 🔧 Troubleshooting

### Problem: "403 Forbidden" on all requests

**Solution:** Make sure you're including the JWT token in the Authorization header:
```
Authorization: Bearer YOUR_TOKEN_HERE
```

### Problem: Token expired

**Solution:** Login again to get a new token. Tokens expire after 24 hours.

### Problem: "Invalid credentials"

**Solution:** Check username and password. Ensure user exists and is enabled.

### Problem: Can't access endpoint with correct role

**Solution:** Check the token role claim. Logout and login again to refresh.

---

## 📝 Best Practices

1. **Never share JWT tokens** - Each user should have their own token
2. **Store tokens securely** - Use httpOnly cookies or secure storage
3. **Change default passwords** - Immediately change default user passwords
4. **Use HTTPS in production** - Always use SSL/TLS for API calls
5. **Rotate secrets regularly** - Change JWT secret periodically
6. **Monitor access logs** - Track who accesses what
7. **Implement token refresh** - Consider shorter expiry with refresh tokens
8. **Validate on both sides** - Client and server validation
9. **Limit failed login attempts** - Prevent brute force attacks
10. **Regular security audits** - Review access controls periodically

---

## 🎓 Additional Resources

- **Spring Security:** https://spring.io/projects/spring-security
- **JWT Introduction:** https://jwt.io/introduction
- **OWASP API Security:** https://owasp.org/www-project-api-security/

---

## ✅ Security Checklist

Before deploying to production:

- [ ] Change all default passwords
- [ ] Generate new JWT secret key
- [ ] Enable HTTPS/TLS
- [ ] Configure CORS properly
- [ ] Set up rate limiting
- [ ] Enable security headers
- [ ] Configure session timeout
- [ ] Set up logging and monitoring
- [ ] Implement account lockout policy
- [ ] Regular security updates
- [ ] Penetration testing
- [ ] Security training for staff

---

**🔐 Your MediTracker API is now secured with enterprise-grade authentication and authorization!**

