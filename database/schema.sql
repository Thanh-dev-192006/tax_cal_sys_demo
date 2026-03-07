-- =====================================================
-- Tax Return System - Database Schema
-- =====================================================

-- Tao database
CREATE DATABASE IF NOT EXISTS tax_return_system;
USE tax_return_system;

-- =====================================================
-- Table: Users
-- =====================================================
CREATE TABLE IF NOT EXISTS Users (
    staff_id VARCHAR(10) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    full_name VARCHAR(100),
    email VARCHAR(100),
    phone_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- Table: Clients
-- =====================================================
CREATE TABLE IF NOT EXISTS Clients (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    income DECIMAL(12, 2) NOT NULL,
    dependents INT DEFAULT 0,
    marital_status VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    phone_number VARCHAR(20),
    city VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- Table: TaxReturns
-- =====================================================
CREATE TABLE IF NOT EXISTS TaxReturns (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id VARCHAR(20) NOT NULL,
    filing_date DATE NOT NULL,
    tax_liability DECIMAL(12, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    marital_status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES Clients(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- Indexes for better performance
-- =====================================================
CREATE INDEX idx_username ON Users(username);
CREATE INDEX idx_client_name ON Clients(name);
CREATE INDEX idx_client_id ON TaxReturns(client_id);
CREATE INDEX idx_filing_date ON TaxReturns(filing_date);