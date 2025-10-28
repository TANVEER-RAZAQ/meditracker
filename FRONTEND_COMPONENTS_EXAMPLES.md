# 🎨 Frontend Component Examples

## Reusable Components for MediTracker

Copy-paste these components to speed up your development!

---

## **1. Button Component**

**File: `src/components/common/Button.jsx`**

```javascript
import React from 'react';

function Button({ 
  children, 
  onClick, 
  type = 'button', 
  variant = 'primary', 
  disabled = false,
  className = '' 
}) {
  const baseStyles = 'px-6 py-3 rounded-lg font-semibold transition-colors disabled:opacity-50 disabled:cursor-not-allowed';
  
  const variants = {
    primary: 'bg-blue-600 text-white hover:bg-blue-700',
    secondary: 'bg-gray-200 text-gray-800 hover:bg-gray-300',
    success: 'bg-green-600 text-white hover:bg-green-700',
    danger: 'bg-red-600 text-white hover:bg-red-700',
    warning: 'bg-orange-600 text-white hover:bg-orange-700',
  };

  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`${baseStyles} ${variants[variant]} ${className}`}
    >
      {children}
    </button>
  );
}

export default Button;
```

**Usage:**
```javascript
<Button variant="primary" onClick={handleClick}>
  Click Me
</Button>

<Button variant="danger" disabled>
  Disabled Button
</Button>
```

---

## **2. Input Component**

**File: `src/components/common/Input.jsx`**

```javascript
import React from 'react';

function Input({ 
  label, 
  type = 'text', 
  value, 
  onChange, 
  placeholder = '', 
  required = false,
  error = '',
  className = '' 
}) {
  return (
    <div className={`mb-4 ${className}`}>
      {label && (
        <label className="block text-sm font-medium text-gray-700 mb-2">
          {label} {required && <span className="text-red-500">*</span>}
        </label>
      )}
      <input
        type={type}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        required={required}
        className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent ${
          error ? 'border-red-500' : 'border-gray-300'
        }`}
      />
      {error && (
        <p className="mt-1 text-sm text-red-600">{error}</p>
      )}
    </div>
  );
}

export default Input;
```

**Usage:**
```javascript
<Input
  label="Full Name"
  value={name}
  onChange={(e) => setName(e.target.value)}
  placeholder="Enter full name"
  required
  error={errors.name}
/>
```

---

## **3. Card Component**

**File: `src/components/common/Card.jsx`**

```javascript
import React from 'react';

function Card({ title, children, className = '' }) {
  return (
    <div className={`bg-white rounded-lg shadow-md overflow-hidden ${className}`}>
      {title && (
        <div className="px-6 py-4 bg-gray-50 border-b border-gray-200">
          <h3 className="text-lg font-semibold text-gray-800">{title}</h3>
        </div>
      )}
      <div className="p-6">
        {children}
      </div>
    </div>
  );
}

export default Card;
```

**Usage:**
```javascript
<Card title="Patient Information">
  <p>Name: John Doe</p>
  <p>RFID: RFID_12345</p>
</Card>
```

---

## **4. Table Component**

**File: `src/components/common/Table.jsx`**

```javascript
import React from 'react';

function Table({ columns, data, onRowClick }) {
  return (
    <div className="overflow-x-auto">
      <table className="min-w-full bg-white">
        <thead className="bg-blue-600 text-white">
          <tr>
            {columns.map((column, index) => (
              <th 
                key={index} 
                className="px-6 py-3 text-left text-sm font-semibold"
              >
                {column.header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="divide-y divide-gray-200">
          {data.length === 0 ? (
            <tr>
              <td 
                colSpan={columns.length} 
                className="px-6 py-8 text-center text-gray-500"
              >
                No data available
              </td>
            </tr>
          ) : (
            data.map((row, rowIndex) => (
              <tr 
                key={rowIndex} 
                onClick={() => onRowClick && onRowClick(row)}
                className={onRowClick ? 'hover:bg-gray-50 cursor-pointer' : ''}
              >
                {columns.map((column, colIndex) => (
                  <td key={colIndex} className="px-6 py-4 text-sm text-gray-900">
                    {column.render ? column.render(row) : row[column.accessor]}
                  </td>
                ))}
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default Table;
```

**Usage:**
```javascript
const columns = [
  { header: 'ID', accessor: 'id' },
  { header: 'Name', accessor: 'fullName' },
  { 
    header: 'Status', 
    render: (row) => (
      <span className={`px-2 py-1 rounded ${
        row.status === 'COMPLETED' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'
      }`}>
        {row.status}
      </span>
    )
  },
];

<Table columns={columns} data={patients} onRowClick={handlePatientClick} />
```

---

## **5. Modal Component**

**File: `src/components/common/Modal.jsx`**

```javascript
import React from 'react';

function Modal({ isOpen, onClose, title, children, size = 'medium' }) {
  if (!isOpen) return null;

  const sizes = {
    small: 'max-w-md',
    medium: 'max-w-2xl',
    large: 'max-w-4xl',
  };

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto">
      {/* Backdrop */}
      <div 
        className="fixed inset-0 bg-black bg-opacity-50 transition-opacity"
        onClick={onClose}
      />
      
      {/* Modal */}
      <div className="flex min-h-full items-center justify-center p-4">
        <div className={`relative bg-white rounded-lg shadow-xl ${sizes[size]} w-full`}>
          {/* Header */}
          <div className="flex items-center justify-between p-6 border-b">
            <h2 className="text-xl font-semibold text-gray-900">{title}</h2>
            <button
              onClick={onClose}
              className="text-gray-400 hover:text-gray-600 text-2xl"
            >
              ×
            </button>
          </div>
          
          {/* Content */}
          <div className="p-6">
            {children}
          </div>
        </div>
      </div>
    </div>
  );
}

export default Modal;
```

**Usage:**
```javascript
const [isOpen, setIsOpen] = useState(false);

<Modal 
  isOpen={isOpen} 
  onClose={() => setIsOpen(false)} 
  title="Patient Details"
  size="large"
>
  <p>Patient information goes here...</p>
</Modal>
```

---

## **6. Loading Spinner**

**File: `src/components/common/Spinner.jsx`**

```javascript
import React from 'react';

function Spinner({ size = 'medium', text = '' }) {
  const sizes = {
    small: 'h-6 w-6',
    medium: 'h-12 w-12',
    large: 'h-16 w-16',
  };

  return (
    <div className="flex flex-col items-center justify-center p-8">
      <div className={`${sizes[size]} border-4 border-blue-200 border-t-blue-600 rounded-full animate-spin`} />
      {text && <p className="mt-4 text-gray-600">{text}</p>}
    </div>
  );
}

export default Spinner;
```

**Usage:**
```javascript
{loading && <Spinner size="large" text="Loading patients..." />}
```

---

## **7. Alert/Notification Component**

**File: `src/components/common/Alert.jsx`**

```javascript
import React from 'react';

function Alert({ type = 'info', message, onClose }) {
  const types = {
    success: 'bg-green-100 border-green-500 text-green-800',
    error: 'bg-red-100 border-red-500 text-red-800',
    warning: 'bg-yellow-100 border-yellow-500 text-yellow-800',
    info: 'bg-blue-100 border-blue-500 text-blue-800',
  };

  const icons = {
    success: '✅',
    error: '❌',
    warning: '⚠️',
    info: 'ℹ️',
  };

  return (
    <div className={`border-l-4 p-4 rounded ${types[type]} flex items-start justify-between`}>
      <div className="flex items-start">
        <span className="text-xl mr-3">{icons[type]}</span>
        <p>{message}</p>
      </div>
      {onClose && (
        <button 
          onClick={onClose}
          className="text-xl font-bold hover:opacity-70"
        >
          ×
        </button>
      )}
    </div>
  );
}

export default Alert;
```

**Usage:**
```javascript
<Alert type="success" message="Patient registered successfully!" />
<Alert type="error" message="Failed to load data" onClose={() => setError(null)} />
```

---

## **8. Badge Component**

**File: `src/components/common/Badge.jsx`**

```javascript
import React from 'react';

function Badge({ children, variant = 'default' }) {
  const variants = {
    default: 'bg-gray-100 text-gray-800',
    success: 'bg-green-100 text-green-800',
    warning: 'bg-yellow-100 text-yellow-800',
    danger: 'bg-red-100 text-red-800',
    info: 'bg-blue-100 text-blue-800',
  };

  return (
    <span className={`px-3 py-1 rounded-full text-xs font-semibold ${variants[variant]}`}>
      {children}
    </span>
  );
}

export default Badge;
```

**Usage:**
```javascript
<Badge variant="success">COMPLETED</Badge>
<Badge variant="warning">PENDING</Badge>
<Badge variant="danger">FAILED</Badge>
```

---

## **9. Status Indicator**

**File: `src/components/common/StatusIndicator.jsx`**

```javascript
import React from 'react';

function StatusIndicator({ status }) {
  const statusConfig = {
    REGISTERED: { color: 'bg-blue-100 text-blue-800', icon: '📋' },
    VITALS: { color: 'bg-purple-100 text-purple-800', icon: '❤️' },
    CONSULTATION: { color: 'bg-indigo-100 text-indigo-800', icon: '👨‍⚕️' },
    LAB_PENDING: { color: 'bg-yellow-100 text-yellow-800', icon: '⏳' },
    LAB_IN_PROGRESS: { color: 'bg-orange-100 text-orange-800', icon: '🔬' },
    LAB_COMPLETED: { color: 'bg-green-100 text-green-800', icon: '✅' },
    BILLING_PENDING: { color: 'bg-red-100 text-red-800', icon: '💳' },
    COMPLETED: { color: 'bg-green-100 text-green-800', icon: '🎉' },
  };

  const config = statusConfig[status] || { color: 'bg-gray-100 text-gray-800', icon: '❓' };

  return (
    <span className={`inline-flex items-center px-3 py-1 rounded-full text-sm font-medium ${config.color}`}>
      <span className="mr-2">{config.icon}</span>
      {status.replace(/_/g, ' ')}
    </span>
  );
}

export default StatusIndicator;
```

**Usage:**
```javascript
<StatusIndicator status="REGISTERED" />
<StatusIndicator status="LAB_IN_PROGRESS" />
<StatusIndicator status="COMPLETED" />
```

---

## **10. Search Bar**

**File: `src/components/common/SearchBar.jsx`**

```javascript
import React from 'react';

function SearchBar({ value, onChange, placeholder = 'Search...', onClear }) {
  return (
    <div className="relative">
      <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
        <span className="text-gray-400">🔍</span>
      </div>
      <input
        type="text"
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        className="w-full pl-10 pr-10 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
      />
      {value && (
        <button
          onClick={onClear}
          className="absolute inset-y-0 right-0 pr-3 flex items-center text-gray-400 hover:text-gray-600"
        >
          ×
        </button>
      )}
    </div>
  );
}

export default SearchBar;
```

**Usage:**
```javascript
<SearchBar
  value={searchTerm}
  onChange={(e) => setSearchTerm(e.target.value)}
  onClear={() => setSearchTerm('')}
  placeholder="Search patients..."
/>
```

---

## **11. Stat Card** (for Dashboard)

**File: `src/components/common/StatCard.jsx`**

```javascript
import React from 'react';

function StatCard({ title, value, icon, color = 'blue' }) {
  const colors = {
    blue: 'bg-blue-500',
    green: 'bg-green-500',
    red: 'bg-red-500',
    yellow: 'bg-yellow-500',
    purple: 'bg-purple-500',
  };

  return (
    <div className="bg-white rounded-lg shadow-md p-6">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm text-gray-600 uppercase">{title}</p>
          <p className="text-3xl font-bold text-gray-900 mt-2">{value}</p>
        </div>
        <div className={`${colors[color]} p-4 rounded-full text-white text-2xl`}>
          {icon}
        </div>
      </div>
    </div>
  );
}

export default StatCard;
```

**Usage:**
```javascript
<div className="grid grid-cols-1 md:grid-cols-4 gap-6">
  <StatCard title="Total Patients" value={125} icon="👥" color="blue" />
  <StatCard title="Active Visits" value={12} icon="🏥" color="green" />
  <StatCard title="Pending Labs" value={8} icon="🔬" color="yellow" />
  <StatCard title="Today's Revenue" value="₹45,000" icon="💰" color="purple" />
</div>
```

---

## **12. Empty State**

**File: `src/components/common/EmptyState.jsx`**

```javascript
import React from 'react';

function EmptyState({ icon = '📭', title, message, action }) {
  return (
    <div className="text-center py-12">
      <div className="text-6xl mb-4">{icon}</div>
      <h3 className="text-xl font-semibold text-gray-900 mb-2">{title}</h3>
      <p className="text-gray-600 mb-6">{message}</p>
      {action && action}
    </div>
  );
}

export default EmptyState;
```

**Usage:**
```javascript
<EmptyState
  icon="👥"
  title="No Patients Found"
  message="Register your first patient to get started"
  action={
    <Button onClick={() => navigate('/register')}>
      Register Patient
    </Button>
  }
/>
```

---

## 🎨 **Complete Example: Patient Card**

**File: `src/components/features/PatientCard.jsx`**

```javascript
import React from 'react';
import Card from '../common/Card';
import Badge from '../common/Badge';
import Button from '../common/Button';

function PatientCard({ patient, onViewDetails, onStartVisit }) {
  return (
    <Card>
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <div className="flex items-center gap-3 mb-2">
            <h3 className="text-xl font-bold text-gray-900">{patient.fullName}</h3>
            <Badge variant="info">ID: {patient.id}</Badge>
          </div>
          
          <div className="space-y-2 text-sm text-gray-600">
            <p>🏷️ RFID: <span className="font-mono">{patient.rfidUid}</span></p>
            {patient.phoneNumber && (
              <p>📱 Phone: {patient.phoneNumber}</p>
            )}
            {patient.dateOfBirth && (
              <p>🎂 DOB: {new Date(patient.dateOfBirth).toLocaleDateString()}</p>
            )}
          </div>
        </div>

        <div className="flex gap-2">
          <Button variant="secondary" onClick={() => onViewDetails(patient)}>
            View Details
          </Button>
          <Button variant="primary" onClick={() => onStartVisit(patient)}>
            Start Visit
          </Button>
        </div>
      </div>
    </Card>
  );
}

export default PatientCard;
```

---

## 📦 **Component Index**

Create `src/components/index.js` for easy imports:

```javascript
// Common Components
export { default as Button } from './common/Button';
export { default as Input } from './common/Input';
export { default as Card } from './common/Card';
export { default as Table } from './common/Table';
export { default as Modal } from './common/Modal';
export { default as Spinner } from './common/Spinner';
export { default as Alert } from './common/Alert';
export { default as Badge } from './common/Badge';
export { default as StatusIndicator } from './common/StatusIndicator';
export { default as SearchBar } from './common/SearchBar';
export { default as StatCard } from './common/StatCard';
export { default as EmptyState } from './common/EmptyState';

// Feature Components
export { default as PatientCard } from './features/PatientCard';
```

**Usage:**
```javascript
import { Button, Card, Table, Modal } from './components';
```

---

## 🚀 **Ready to Use!**

Copy these components into your project and customize them as needed!

**See FRONTEND_QUICK_START.md to get your React app running!**

