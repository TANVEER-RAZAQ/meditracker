# 🏥 MediTracker Frontend - Complete Code

## 🎨 Design Inspiration

**Color Palette (from your image):**
- Primary: `#5FA8A8` (Calming Teal)
- Secondary: `#7EC4C4` (Light Teal)
- Accent: `#4A9999` (Deep Teal)
- Background: `#F0F7F7` (Soft Blue-Gray)
- White: `#FFFFFF`
- Text: `#2C3E50` (Dark Blue-Gray)
- Success: `#6BCF9E` (Mint Green)
- Warning: `#F4B860` (Soft Orange)
- Error: `#E57373` (Soft Red)

**Design Principles:**
- Minimalist and clean
- Soft rounded corners
- Subtle shadows
- Smooth animations
- Healthcare-themed icons
- Calming atmosphere

---

## 📁 Complete File Structure

```
meditracker-frontend/
├── public/
│   ├── index.html
│   └── favicon.ico
├── src/
│   ├── components/
│   │   ├── common/
│   │   │   ├── Button.jsx
│   │   │   ├── Input.jsx
│   │   │   ├── Card.jsx
│   │   │   ├── Table.jsx
│   │   │   ├── Modal.jsx
│   │   │   ├── Spinner.jsx
│   │   │   ├── Alert.jsx
│   │   │   ├── Badge.jsx
│   │   │   └── StatusBadge.jsx
│   │   ├── layout/
│   │   │   ├── Header.jsx
│   │   │   ├── Sidebar.jsx
│   │   │   ├── Footer.jsx
│   │   │   └── Layout.jsx
│   │   └── features/
│   │       ├── PatientCard.jsx
│   │       ├── VitalsChart.jsx
│   │       └── StatCard.jsx
│   ├── pages/
│   │   ├── Home.jsx
│   │   ├── RegistrationDesk.jsx
│   │   ├── NurseStation.jsx
│   │   ├── DoctorDashboard.jsx
│   │   ├── LabPortal.jsx
│   │   ├── BillingCounter.jsx
│   │   ├── AdminDashboard.jsx
│   │   ├── DischargeStation.jsx
│   │   └── WalletManagement.jsx
│   ├── services/
│   │   └── api.js
│   ├── utils/
│   │   └── helpers.js
│   ├── App.js
│   ├── App.css
│   └── index.js
├── tailwind.config.js
└── package.json
```

---

## 🚀 Step 1: Initial Setup

### **1. Create React App**

```bash
cd C:/Users/tantr/IdeaProjects
npx create-react-app meditracker-frontend
cd meditracker-frontend
```

### **2. Install Dependencies**

```bash
npm install react-router-dom axios framer-motion lucide-react
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```

---

## 🎨 Step 2: Configure Tailwind CSS

### **File: `tailwind.config.js`**

```javascript
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#E6F4F4',
          100: '#CCE9E9',
          200: '#99D3D3',
          300: '#7EC4C4',
          400: '#5FA8A8',
          500: '#4A9999',
          600: '#3D7F7F',
          700: '#2F6666',
          800: '#224C4C',
          900: '#143333',
        },
        medical: {
          light: '#F0F7F7',
          DEFAULT: '#5FA8A8',
          dark: '#4A9999',
        },
        success: '#6BCF9E',
        warning: '#F4B860',
        error: '#E57373',
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
      boxShadow: {
        'soft': '0 2px 15px -3px rgba(95, 168, 168, 0.1), 0 10px 20px -2px rgba(95, 168, 168, 0.04)',
        'soft-lg': '0 10px 40px -10px rgba(95, 168, 168, 0.2)',
      },
      animation: {
        'fade-in': 'fadeIn 0.5s ease-in-out',
        'slide-up': 'slideUp 0.5s ease-out',
        'pulse-soft': 'pulseSoft 2s ease-in-out infinite',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        slideUp: {
          '0%': { transform: 'translateY(20px)', opacity: '0' },
          '100%': { transform: 'translateY(0)', opacity: '1' },
        },
        pulseSoft: {
          '0%, 100%': { opacity: '1' },
          '50%': { opacity: '0.7' },
        },
      },
    },
  },
  plugins: [],
}
```

### **File: `src/index.css`**

```css
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

@tailwind base;
@tailwind components;
@tailwind utilities;

@layer base {
  body {
    @apply bg-medical-light text-gray-800 font-sans antialiased;
  }
}

@layer components {
  .btn-primary {
    @apply bg-primary-500 hover:bg-primary-600 text-white font-medium px-6 py-3 rounded-lg transition-all duration-300 shadow-soft hover:shadow-soft-lg transform hover:-translate-y-0.5;
  }
  
  .btn-secondary {
    @apply bg-white hover:bg-gray-50 text-primary-600 font-medium px-6 py-3 rounded-lg border-2 border-primary-300 transition-all duration-300;
  }
  
  .card {
    @apply bg-white rounded-2xl shadow-soft p-6 hover:shadow-soft-lg transition-shadow duration-300;
  }
  
  .input-field {
    @apply w-full px-4 py-3 border-2 border-gray-200 rounded-lg focus:border-primary-400 focus:ring-2 focus:ring-primary-100 transition-all duration-200 outline-none;
  }
}

/* Custom Scrollbar */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #5FA8A8;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #4A9999;
}
```

---

## 📄 Step 3: API Service Layer

### **File: `src/services/api.js`**

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Registration APIs
export const registrationAPI = {
  registerPatient: (data) => api.post('/api/registration', data),
};

// Patient APIs
export const patientAPI = {
  getAllPatients: () => api.get('/api/patients'),
  getPatientById: (id) => api.get(`/api/patients/${id}`),
  getPatientByRFID: (rfid) => api.get(`/api/patients/rfid/${rfid}`),
  getPatientVisits: (id) => api.get(`/api/patients/${id}/visits`),
};

// Doctor APIs
export const doctorAPI = {
  getAllDoctors: () => api.get('/api/doctors'),
  getDoctorById: (id) => api.get(`/api/doctors/${id}`),
  getDoctorsByDepartment: (dept) => api.get(`/api/doctors/department/${dept}`),
  createDoctor: (data) => api.post('/api/doctors', data),
  updateDoctor: (id, data) => api.put(`/api/doctors/${id}`, data),
  deleteDoctor: (id) => api.delete(`/api/doctors/${id}`),
};

// Visit APIs
export const visitAPI = {
  getAllVisits: () => api.get('/api/visits'),
  getVisitsByStatus: (status) => api.get(`/api/visits/status/${status}`),
  startVisit: (data) => api.post('/api/visits/start', data),
  recordVitals: (id, data) => api.post(`/api/visits/${id}/vitals`, data),
  addConsultation: (id, data) => api.post(`/api/visits/${id}/consultation`, data),
  dischargePatient: (data) => api.post('/api/visits/discharge', data),
  getVisitSummary: (id) => api.get(`/api/visits/${id}/summary`),
  getPatientHistory: (rfid) => api.get(`/api/visits/history/${rfid}`),
};

// Lab APIs
export const labAPI = {
  getAllLabTests: () => api.get('/api/lab'),
  getLabTestsByStatus: (status) => api.get(`/api/lab/status/${status}`),
  getLabTestsByVisit: (visitId) => api.get(`/api/lab/visit/${visitId}`),
  getLabTest: (testId) => api.get(`/api/lab/${testId}`),
  orderLabTest: (visitId, data) => api.post(`/api/lab/${visitId}/order`, data),
  updateLabStatus: (testId, data) => api.post(`/api/lab/tests/${testId}/status`, data),
};

// Wallet APIs
export const walletAPI = {
  topUpWallet: (data) => api.post('/api/wallet/topup', data),
  getWalletByRFID: (rfid) => api.get(`/api/wallet/rfid/${rfid}`),
  getWalletByPatientId: (id) => api.get(`/api/wallet/patient/${id}`),
};

// Billing APIs
export const billingAPI = {
  payWithRFID: (data) => api.post('/api/billing/pay', data),
  getBillingByVisit: (visitId) => api.get(`/api/billing/visit/${visitId}`),
};

export default api;
```

---

## 🧩 Step 4: Common Components

### **File: `src/components/common/Button.jsx`**

```javascript
import React from 'react';
import { motion } from 'framer-motion';

function Button({ 
  children, 
  onClick, 
  type = 'button', 
  variant = 'primary',
  disabled = false,
  icon: Icon,
  className = '' 
}) {
  const variants = {
    primary: 'btn-primary',
    secondary: 'btn-secondary',
    success: 'bg-success hover:bg-green-600 text-white',
    danger: 'bg-error hover:bg-red-600 text-white',
    ghost: 'bg-transparent hover:bg-primary-50 text-primary-600',
  };

  return (
    <motion.button
      whileHover={{ scale: disabled ? 1 : 1.02 }}
      whileTap={{ scale: disabled ? 1 : 0.98 }}
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`${variants[variant]} px-6 py-3 rounded-lg font-medium transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2 justify-center ${className}`}
    >
      {Icon && <Icon size={20} />}
      {children}
    </motion.button>
  );
}

export default Button;
```

### **File: `src/components/common/Input.jsx`**

```javascript
import React from 'react';
import { motion } from 'framer-motion';

function Input({ 
  label, 
  type = 'text', 
  value, 
  onChange, 
  placeholder = '', 
  required = false,
  error = '',
  icon: Icon,
  className = '' 
}) {
  return (
    <motion.div 
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      className={`mb-4 ${className}`}
    >
      {label && (
        <label className="block text-sm font-medium text-gray-700 mb-2">
          {label} {required && <span className="text-error">*</span>}
        </label>
      )}
      <div className="relative">
        {Icon && (
          <div className="absolute left-3 top-1/2 transform -translate-y-1/2 text-primary-400">
            <Icon size={20} />
          </div>
        )}
        <input
          type={type}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          required={required}
          className={`input-field ${Icon ? 'pl-11' : ''} ${error ? 'border-error focus:border-error focus:ring-error' : ''}`}
        />
      </div>
      {error && (
        <motion.p 
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="mt-1 text-sm text-error"
        >
          {error}
        </motion.p>
      )}
    </motion.div>
  );
}

export default Input;
```

### **File: `src/components/common/Card.jsx`**

```javascript
import React from 'react';
import { motion } from 'framer-motion';

function Card({ title, subtitle, children, className = '', icon: Icon }) {
  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className={`card ${className}`}
    >
      {(title || Icon) && (
        <div className="flex items-start justify-between mb-6 pb-4 border-b border-gray-100">
          <div className="flex items-center gap-3">
            {Icon && (
              <div className="p-2 bg-primary-50 rounded-lg text-primary-600">
                <Icon size={24} />
              </div>
            )}
            <div>
              {title && <h3 className="text-lg font-semibold text-gray-900">{title}</h3>}
              {subtitle && <p className="text-sm text-gray-500 mt-1">{subtitle}</p>}
            </div>
          </div>
        </div>
      )}
      <div>
        {children}
      </div>
    </motion.div>
  );
}

export default Card;
```

---

I'll continue with more components in the next file. Let me create a comprehensive package with all pages.

