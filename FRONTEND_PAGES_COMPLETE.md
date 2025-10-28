# 🏥 MediTracker Frontend - Complete Pages Code

## 📄 All Page Components

---

### **File: `src/pages/Home.jsx`**

```javascript
import React from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import { 
  Heart, 
  Activity, 
  Users, 
  Microscope, 
  CreditCard, 
  UserCheck,
  ArrowRight,
  Sparkles
} from 'lucide-react';
import Button from '../components/common/Button';

function Home() {
  const navigate = useNavigate();

  const features = [
    {
      icon: UserCheck,
      title: 'Registration Desk',
      desc: 'Quick patient registration with RFID',
      path: '/registration',
      color: 'bg-blue-50 text-blue-600',
    },
    {
      icon: Activity,
      title: 'Nurse Station',
      desc: 'Record vitals and patient care',
      path: '/nurse',
      color: 'bg-purple-50 text-purple-600',
    },
    {
      icon: Heart,
      title: 'Doctor Dashboard',
      desc: 'Consultations and diagnoses',
      path: '/doctor',
      color: 'bg-pink-50 text-pink-600',
    },
    {
      icon: Microscope,
      title: 'Lab Portal',
      desc: 'Manage tests and results',
      path: '/lab',
      color: 'bg-green-50 text-green-600',
    },
    {
      icon: CreditCard,
      title: 'Billing Counter',
      desc: 'Process payments and billing',
      path: '/billing',
      color: 'bg-orange-50 text-orange-600',
    },
    {
      icon: Users,
      title: 'Admin Dashboard',
      desc: 'System management and reports',
      path: '/admin',
      color: 'bg-primary-50 text-primary-600',
    },
  ];

  return (
    <div className="min-h-screen">
      {/* Hero Section */}
      <section className="relative overflow-hidden bg-gradient-to-br from-primary-50 via-white to-primary-50">
        <div className="absolute inset-0 opacity-10">
          <div className="absolute top-20 left-10 w-72 h-72 bg-primary-300 rounded-full blur-3xl" />
          <div className="absolute bottom-20 right-10 w-96 h-96 bg-primary-200 rounded-full blur-3xl" />
        </div>
        
        <div className="relative max-w-7xl mx-auto px-6 py-24">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            className="text-center max-w-4xl mx-auto"
          >
            <motion.div
              initial={{ scale: 0 }}
              animate={{ scale: 1 }}
              transition={{ delay: 0.2, type: 'spring' }}
              className="inline-flex items-center gap-2 bg-white px-6 py-2 rounded-full shadow-soft mb-6"
            >
              <Sparkles className="text-primary-500" size={20} />
              <span className="text-primary-700 font-medium">Modern Healthcare Management</span>
            </motion.div>

            <h1 className="text-6xl font-bold text-gray-900 mb-6 leading-tight">
              Welcome to{' '}
              <span className="text-primary-500">MediTracker</span>
            </h1>
            
            <p className="text-xl text-gray-600 mb-12 leading-relaxed max-w-2xl mx-auto">
              A comprehensive hospital management system with RFID-based patient flow, 
              digital wallet payments, and complete healthcare workflow automation.
            </p>

            <div className="flex gap-4 justify-center flex-wrap">
              <Button 
                variant="primary" 
                onClick={() => navigate('/registration')}
                icon={UserCheck}
              >
                Get Started
              </Button>
              <Button 
                variant="secondary" 
                onClick={() => navigate('/admin')}
                icon={Users}
              >
                Admin Dashboard
              </Button>
            </div>
          </motion.div>
        </div>
      </section>

      {/* Features Grid */}
      <section className="max-w-7xl mx-auto px-6 py-20">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
          className="text-center mb-16"
        >
          <h2 className="text-4xl font-bold text-gray-900 mb-4">
            Complete Healthcare Solutions
          </h2>
          <p className="text-gray-600 text-lg max-w-2xl mx-auto">
            Access all modules from one unified platform. Streamline your hospital operations.
          </p>
        </motion.div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {features.map((feature, index) => (
            <motion.div
              key={index}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.4 + index * 0.1 }}
              whileHover={{ y: -8 }}
              onClick={() => navigate(feature.path)}
              className="card cursor-pointer group"
            >
              <div className={`inline-flex p-4 rounded-2xl ${feature.color} mb-4 group-hover:scale-110 transition-transform duration-300`}>
                <feature.icon size={28} />
              </div>
              <h3 className="text-xl font-semibold text-gray-900 mb-2">
                {feature.title}
              </h3>
              <p className="text-gray-600 mb-4">
                {feature.desc}
              </p>
              <div className="flex items-center text-primary-600 font-medium group-hover:gap-3 gap-2 transition-all">
                Access Module
                <ArrowRight size={16} className="group-hover:translate-x-1 transition-transform" />
              </div>
            </motion.div>
          ))}
        </div>
      </section>

      {/* Stats Section */}
      <section className="bg-gradient-to-br from-primary-500 to-primary-600 py-20">
        <div className="max-w-7xl mx-auto px-6">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8 text-center">
            {[
              { label: 'Active Patients', value: '500+', icon: Users },
              { label: 'Doctors', value: '50+', icon: Heart },
              { label: 'Lab Tests/Month', value: '1000+', icon: Microscope },
              { label: 'Visits/Day', value: '200+', icon: Activity },
            ].map((stat, index) => (
              <motion.div
                key={index}
                initial={{ opacity: 0, scale: 0.5 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ delay: 0.6 + index * 0.1 }}
                className="text-white"
              >
                <stat.icon className="mx-auto mb-4" size={40} />
                <div className="text-4xl font-bold mb-2">{stat.value}</div>
                <div className="text-primary-100">{stat.label}</div>
              </motion.div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
}

export default Home;
```

---

### **File: `src/pages/RegistrationDesk.jsx`**

```javascript
import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { UserPlus, Search, CreditCard, User, Phone, Calendar } from 'lucide-react';
import Card from '../components/common/Card';
import Input from '../components/common/Input';
import Button from '../components/common/Button';
import Alert from '../components/common/Alert';
import { patientAPI, visitAPI, doctorAPI } from '../services/api';

function RegistrationDesk() {
  const [activeTab, setActiveTab] = useState('register');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({ type: '', text: '' });
  
  // Registration form
  const [formData, setFormData] = useState({
    fullName: '',
    rfidUid: '',
    phoneNumber: '',
    dateOfBirth: '',
  });

  // Search
  const [searchRFID, setSearchRFID] = useState('');
  const [patient, setPatient] = useState(null);
  const [doctors, setDoctors] = useState([]);

  const handleRegister = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage({ type: '', text: '' });

    try {
      const response = await patientAPI.registerPatient(formData);
      setMessage({ 
        type: 'success', 
        text: `✅ Patient registered successfully! ID: ${response.data.id}` 
      });
      setFormData({ fullName: '', rfidUid: '', phoneNumber: '', dateOfBirth: '' });
    } catch (error) {
      setMessage({ 
        type: 'error', 
        text: error.response?.data?.message || 'Failed to register patient' 
      });
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (!searchRFID) return;
    setLoading(true);
    try {
      const response = await patientAPI.getPatientByRFID(searchRFID);
      setPatient(response.data);
      // Load doctors for visit
      const docResponse = await doctorAPI.getAllDoctors();
      setDoctors(docResponse.data);
    } catch (error) {
      setMessage({ type: 'error', text: 'Patient not found' });
    } finally {
      setLoading(false);
    }
  };

  const handleStartVisit = async (department) => {
    setLoading(true);
    try {
      await visitAPI.startVisit({
        rfidUid: patient.rfidUid,
        department: department
      });
      setMessage({ type: 'success', text: '✅ Visit started successfully!' });
      setPatient(null);
      setSearchRFID('');
    } catch (error) {
      setMessage({ type: 'error', text: 'Failed to start visit' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-7xl mx-auto p-6">
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        className="mb-8"
      >
        <h1 className="text-4xl font-bold text-gray-900 mb-2">Registration Desk</h1>
        <p className="text-gray-600">Register new patients or search existing records</p>
      </motion.div>

      {/* Tabs */}
      <div className="flex gap-4 mb-6">
        <button
          onClick={() => setActiveTab('register')}
          className={`px-6 py-3 rounded-lg font-medium transition-all ${
            activeTab === 'register'
              ? 'bg-primary-500 text-white shadow-soft'
              : 'bg-white text-gray-600 hover:bg-gray-50'
          }`}
        >
          <UserPlus className="inline mr-2" size={20} />
          Register Patient
        </button>
        <button
          onClick={() => setActiveTab('search')}
          className={`px-6 py-3 rounded-lg font-medium transition-all ${
            activeTab === 'search'
              ? 'bg-primary-500 text-white shadow-soft'
              : 'bg-white text-gray-600 hover:bg-gray-50'
          }`}
        >
          <Search className="inline mr-2" size={20} />
          Search Patient
        </button>
      </div>

      {message.text && (
        <Alert type={message.type} message={message.text} onClose={() => setMessage({ type: '', text: '' })} />
      )}

      {/* Register Tab */}
      {activeTab === 'register' && (
        <Card title="New Patient Registration" icon={UserPlus}>
          <form onSubmit={handleRegister} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <Input
                label="Full Name"
                value={formData.fullName}
                onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
                placeholder="John Doe"
                required
                icon={User}
              />
              <Input
                label="RFID Card Number"
                value={formData.rfidUid}
                onChange={(e) => setFormData({ ...formData, rfidUid: e.target.value })}
                placeholder="RFID_12345"
                required
                icon={CreditCard}
              />
              <Input
                label="Phone Number"
                type="tel"
                value={formData.phoneNumber}
                onChange={(e) => setFormData({ ...formData, phoneNumber: e.target.value })}
                placeholder="+91 98765 43210"
                icon={Phone}
              />
              <Input
                label="Date of Birth"
                type="date"
                value={formData.dateOfBirth}
                onChange={(e) => setFormData({ ...formData, dateOfBirth: e.target.value })}
                icon={Calendar}
              />
            </div>
            <Button type="submit" variant="primary" disabled={loading} className="w-full">
              {loading ? 'Registering...' : 'Register Patient'}
            </Button>
          </form>
        </Card>
      )}

      {/* Search Tab */}
      {activeTab === 'search' && (
        <div className="space-y-6">
          <Card title="Search Patient by RFID" icon={Search}>
            <div className="flex gap-4">
              <Input
                value={searchRFID}
                onChange={(e) => setSearchRFID(e.target.value)}
                placeholder="Enter RFID number"
                icon={CreditCard}
                className="flex-1"
              />
              <Button onClick={handleSearch} disabled={loading}>
                Search
              </Button>
            </div>
          </Card>

          {patient && (
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
            >
              <Card title="Patient Details" icon={User}>
                <div className="grid grid-cols-2 gap-4 mb-6">
                  <div>
                    <p className="text-sm text-gray-600">Patient ID</p>
                    <p className="font-semibold">{patient.id}</p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-600">Full Name</p>
                    <p className="font-semibold">{patient.fullName}</p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-600">RFID</p>
                    <p className="font-semibold font-mono">{patient.rfidUid}</p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-600">Phone</p>
                    <p className="font-semibold">{patient.phoneNumber || 'N/A'}</p>
                  </div>
                </div>

                <div className="border-t pt-6">
                  <h4 className="font-semibold mb-4">Start New Visit - Select Department:</h4>
                  <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
                    {['CARDIOLOGY', 'GENERAL_MEDICINE', 'NEUROLOGY', 'ORTHOPEDICS', 'PEDIATRICS', 'DERMATOLOGY'].map(dept => (
                      <Button
                        key={dept}
                        variant="secondary"
                        onClick={() => handleStartVisit(dept)}
                        disabled={loading}
                      >
                        {dept.replace('_', ' ')}
                      </Button>
                    ))}
                  </div>
                </div>
              </Card>
            </motion.div>
          )}
        </div>
      )}
    </div>
  );
}

export default RegistrationDesk;
```

---

I need to create more pages. Let me continue with a comprehensive file containing all remaining pages...

