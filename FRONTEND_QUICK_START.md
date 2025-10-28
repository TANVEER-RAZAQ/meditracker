# 🚀 Frontend Quick Start Guide

## ⚡ **Get Started in 10 Minutes!**

Follow these steps to create your frontend right now!

---

## **Step 1: Create React App** (2 minutes)

Open a **new terminal** (not in the backend folder):

```bash
# Navigate to your projects folder
cd C:\Users\tantr\IdeaProjects

# Create new React app
npx create-react-app meditracker-frontend

# Navigate into the project
cd meditracker-frontend
```

---

## **Step 2: Install Dependencies** (1 minute)

```bash
npm install react-router-dom axios
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```

---

## **Step 3: Configure Tailwind CSS** (1 minute)

**Edit `tailwind.config.js`:**
```javascript
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

**Edit `src/index.css`:**
```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```

---

## **Step 4: Create API Service** (3 minutes)

**Create `src/services/api.js`:**

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Patient APIs
export const patientAPI = {
  getAllPatients: () => api.get('/api/patients'),
  getPatientByRFID: (rfid) => api.get(`/api/patients/rfid/${rfid}`),
  registerPatient: (data) => api.post('/api/registration', data),
};

// Doctor APIs
export const doctorAPI = {
  getAllDoctors: () => api.get('/api/doctors'),
  createDoctor: (data) => api.post('/api/doctors', data),
};

// Visit APIs
export const visitAPI = {
  startVisit: (data) => api.post('/api/visits/start', data),
  recordVitals: (id, data) => api.post(`/api/visits/${id}/vitals`, data),
  addConsultation: (id, data) => api.post(`/api/visits/${id}/consultation`, data),
};

export default api;
```

---

## **Step 5: Create First Page - Patient Registration** (3 minutes)

**Create `src/pages/PatientRegistration.jsx`:**

```javascript
import React, { useState } from 'react';
import { patientAPI } from '../services/api';

function PatientRegistration() {
  const [formData, setFormData] = useState({
    fullName: '',
    rfidUid: '',
    phoneNumber: '',
  });
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');

    try {
      const response = await patientAPI.registerPatient(formData);
      setMessage(`✅ Patient registered successfully! ID: ${response.data.id}`);
      setFormData({ fullName: '', rfidUid: '', phoneNumber: '' });
    } catch (error) {
      setMessage(`❌ Error: ${error.response?.data?.message || error.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 py-12 px-4">
      <div className="max-w-md mx-auto bg-white rounded-lg shadow-md p-8">
        <h1 className="text-3xl font-bold text-blue-600 mb-6">
          🏥 Patient Registration
        </h1>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Full Name *
            </label>
            <input
              type="text"
              value={formData.fullName}
              onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              RFID Card Number *
            </label>
            <input
              type="text"
              value={formData.rfidUid}
              onChange={(e) => setFormData({ ...formData, rfidUid: e.target.value })}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              placeholder="RFID_12345"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Phone Number
            </label>
            <input
              type="tel"
              value={formData.phoneNumber}
              onChange={(e) => setFormData({ ...formData, phoneNumber: e.target.value })}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              placeholder="+91 98765 43210"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 disabled:bg-gray-400 transition-colors"
          >
            {loading ? '⏳ Registering...' : '✅ Register Patient'}
          </button>
        </form>

        {message && (
          <div className={`mt-4 p-4 rounded-lg ${
            message.includes('✅') ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
          }`}>
            {message}
          </div>
        )}
      </div>
    </div>
  );
}

export default PatientRegistration;
```

---

## **Step 6: Update App.js** (1 minute)

**Replace `src/App.js`:**

```javascript
import React from 'react';
import PatientRegistration from './pages/PatientRegistration';

function App() {
  return (
    <div className="App">
      <PatientRegistration />
    </div>
  );
}

export default App;
```

---

## **Step 7: Start Development Server** (1 minute)

```bash
npm start
```

**Your browser will open at:** `http://localhost:3000`

---

## **Step 8: Test Your First Feature!** ✅

1. Make sure your **backend is running** on `http://localhost:8080`
2. Fill in the patient registration form
3. Click "Register Patient"
4. You should see a success message!

---

## 🎉 **Congratulations!**

You now have:
- ✅ React app running
- ✅ Tailwind CSS configured
- ✅ API service setup
- ✅ First page working
- ✅ Backend integration

---

## 📚 **What's Next?**

### **Add More Pages:**

1. **Patient List** - Show all patients
2. **Start Visit** - Begin a patient visit
3. **Record Vitals** - Nurse station
4. **Doctor Dashboard** - Consultations
5. **Lab Portal** - Lab tests

### **Follow the Complete Roadmap:**
See `FRONTEND_ROADMAP.md` for the complete 6-week plan!

---

## 🗂️ **Your Current Project Structure:**

```
meditracker-frontend/
├── public/
├── src/
│   ├── pages/
│   │   └── PatientRegistration.jsx  ✅ Created
│   ├── services/
│   │   └── api.js                   ✅ Created
│   ├── App.js                       ✅ Updated
│   ├── index.css                    ✅ Updated
│   └── index.js
├── package.json
└── tailwind.config.js               ✅ Created
```

---

## 💡 **Quick Tips:**

1. **Backend Must Run First** - Start `mvn spring-boot:run` before testing frontend
2. **CORS Issues?** - Add CORS configuration to backend if needed
3. **Port 3000** - Frontend runs on 3000, backend on 8080
4. **Hot Reload** - Changes auto-reload in development
5. **Console Errors** - Check browser console for errors

---

## 🛠️ **Common Commands:**

```bash
# Start development server
npm start

# Build for production
npm run build

# Install new package
npm install package-name

# Stop server
Ctrl + C
```

---

## 🎯 **Next Feature to Add:**

**Patient List Page:**

**Create `src/pages/PatientList.jsx`:**

```javascript
import React, { useState, useEffect } from 'react';
import { patientAPI } from '../services/api';

function PatientList() {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadPatients();
  }, []);

  const loadPatients = async () => {
    try {
      const response = await patientAPI.getAllPatients();
      setPatients(response.data);
    } catch (error) {
      console.error('Error loading patients:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="text-center p-8">Loading...</div>;

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <div className="max-w-6xl mx-auto">
        <h1 className="text-3xl font-bold text-blue-600 mb-6">
          👥 All Patients
        </h1>

        <div className="bg-white rounded-lg shadow-md overflow-hidden">
          <table className="min-w-full">
            <thead className="bg-blue-600 text-white">
              <tr>
                <th className="px-6 py-3 text-left">ID</th>
                <th className="px-6 py-3 text-left">Name</th>
                <th className="px-6 py-3 text-left">RFID</th>
                <th className="px-6 py-3 text-left">Phone</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {patients.map((patient) => (
                <tr key={patient.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4">{patient.id}</td>
                  <td className="px-6 py-4 font-medium">{patient.fullName}</td>
                  <td className="px-6 py-4">{patient.rfidUid}</td>
                  <td className="px-6 py-4">{patient.phoneNumber || 'N/A'}</td>
                </tr>
              ))}
            </tbody>
          </table>

          {patients.length === 0 && (
            <div className="text-center py-8 text-gray-500">
              No patients found. Register some patients first!
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default PatientList;
```

**Add routing in `App.js`:**

```javascript
import React from 'react';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import PatientRegistration from './pages/PatientRegistration';
import PatientList from './pages/PatientList';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        {/* Navigation */}
        <nav className="bg-blue-600 text-white p-4">
          <div className="max-w-7xl mx-auto flex gap-6">
            <Link to="/" className="hover:underline">Register Patient</Link>
            <Link to="/patients" className="hover:underline">Patient List</Link>
          </div>
        </nav>

        {/* Routes */}
        <Routes>
          <Route path="/" element={<PatientRegistration />} />
          <Route path="/patients" element={<PatientList />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
```

---

## 🚀 **You're All Set!**

- ✅ React app created
- ✅ First page working
- ✅ API integration complete
- ✅ Ready to build more features!

**Follow FRONTEND_ROADMAP.md for the complete development plan!**

**Happy Coding!** 🎉👨‍💻

