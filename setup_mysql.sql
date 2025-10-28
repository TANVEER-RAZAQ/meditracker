-- Setup script for MediTracker MySQL Database
-- Run this in MySQL command line or MySQL Workbench

-- Create database
CREATE DATABASE IF NOT EXISTS meditracker
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

-- Use the database
USE meditracker;

-- Show confirmation
SELECT 'Database meditracker created/selected successfully!' AS Status;

