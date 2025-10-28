#!/bin/bash

# MediTracker Frontend Setup Script
echo "🏥 Setting up MediTracker Frontend..."

# Navigate to projects directory
cd "C:/Users/tantr/IdeaProjects" || exit

# Create React app
echo "📦 Creating React application..."
npx create-react-app meditracker-frontend --template cra-template

# Navigate into project
cd meditracker-frontend || exit

# Install dependencies
echo "📚 Installing dependencies..."
npm install react-router-dom axios framer-motion lucide-react
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p

echo "✅ Frontend setup complete!"
echo "🚀 Run 'npm start' to begin development"

