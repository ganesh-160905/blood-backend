-- Create database if not exists
CREATE DATABASE IF NOT EXISTS lifeline_link_db;
USE lifeline_link_db;

-- Insert sample blood banks
INSERT INTO blood_banks (name, address, city, state, phone, email, latitude, longitude, open_hours, is_verified, rating) VALUES
('Central Blood Bank', '123 Medical Center Dr', 'New York', 'NY', '+1-555-0101', 'central@bloodbank.com', 40.7128, -74.0060, '24/7', true, 4.8),
('City Hospital Blood Bank', '456 Health Ave', 'Los Angeles', 'CA', '+1-555-0102', 'city@hospital.com', 34.0522, -118.2437, '6:00 AM - 10:00 PM', true, 4.5),
('Regional Blood Center', '789 Life Way', 'Chicago', 'IL', '+1-555-0103', 'regional@bloodcenter.org', 41.8781, -87.6298, '24/7', true, 4.7),
('Community Blood Drive', '321 Care St', 'Houston', 'TX', '+1-555-0104', 'community@blooddrive.org', 29.7604, -95.3698, '8:00 AM - 6:00 PM', true, 4.6),
('Metro Blood Services', '654 Donation Blvd', 'Phoenix', 'AZ', '+1-555-0105', 'metro@bloodservices.com', 33.4484, -112.0740, '7:00 AM - 9:00 PM', true, 4.4);

-- Insert blood inventory for each blood bank
INSERT INTO blood_inventory (blood_bank_id, blood_type, units_available, last_updated) VALUES
-- Central Blood Bank inventory
(1, 'A+', 15, NOW()),
(1, 'A-', 8, NOW()),
(1, 'B+', 12, NOW()),
(1, 'B-', 5, NOW()),
(1, 'AB+', 7, NOW()),
(1, 'AB-', 3, NOW()),
(1, 'O+', 25, NOW()),
(1, 'O-', 10, NOW()),

-- City Hospital Blood Bank inventory
(2, 'A+', 18, NOW()),
(2, 'A-', 6, NOW()),
(2, 'B+', 14, NOW()),
(2, 'B-', 4, NOW()),
(2, 'AB+', 9, NOW()),
(2, 'AB-', 2, NOW()),
(2, 'O+', 22, NOW()),
(2, 'O-', 8, NOW()),

-- Regional Blood Center inventory
(3, 'A+', 20, NOW()),
(3, 'A-', 10, NOW()),
(3, 'B+', 16, NOW()),
(3, 'B-', 7, NOW()),
(3, 'AB+', 11, NOW()),
(3, 'AB-', 4, NOW()),
(3, 'O+', 30, NOW()),
(3, 'O-', 12, NOW()),

-- Community Blood Drive inventory
(4, 'A+', 12, NOW()),
(4, 'A-', 5, NOW()),
(4, 'B+', 10, NOW()),
(4, 'B-', 3, NOW()),
(4, 'AB+', 6, NOW()),
(4, 'AB-', 2, NOW()),
(4, 'O+', 18, NOW()),
(4, 'O-', 7, NOW()),

-- Metro Blood Services inventory
(5, 'A+', 16, NOW()),
(5, 'A-', 9, NOW()),
(5, 'B+', 13, NOW()),
(5, 'B-', 6, NOW()),
(5, 'AB+', 8, NOW()),
(5, 'AB-', 3, NOW()),
(5, 'O+', 24, NOW()),
(5, 'O-', 9, NOW());

-- Create an admin user (password: admin123)
INSERT INTO users (first_name, last_name, email, phone, password, blood_type, age, city, state, role, created_at, updated_at) VALUES
('Admin', 'User', 'admin@lifelinelink.com', '+1-555-ADMIN', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'O+', 30, 'New York', 'NY', 'ADMIN', NOW(), NOW());

-- Create some sample users for testing (password: password123)
INSERT INTO users (first_name, last_name, email, phone, password, blood_type, age, city, state, role, is_available, total_donations, points, created_at, updated_at) VALUES
('John', 'Doe', 'john.doe@email.com', '+1-555-0201', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'O+', 28, 'New York', 'NY', 'USER', true, 5, 500, NOW(), NOW()),
('Jane', 'Smith', 'jane.smith@email.com', '+1-555-0202', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'A+', 32, 'Los Angeles', 'CA', 'USER', true, 8, 800, NOW(), NOW()),
('Mike', 'Johnson', 'mike.johnson@email.com', '+1-555-0203', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'B+', 25, 'Chicago', 'IL', 'USER', true, 3, 300, NOW(), NOW()),
('Sarah', 'Williams', 'sarah.williams@email.com', '+1-555-0204', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'AB+', 29, 'Houston', 'TX', 'USER', true, 12, 1200, NOW(), NOW()),
('David', 'Brown', 'david.brown@email.com', '+1-555-0205', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'O-', 35, 'Phoenix', 'AZ', 'USER', true, 15, 1500, NOW(), NOW());

-- Insert sample blood donations
INSERT INTO blood_donations (user_id, blood_bank_id, donation_date, blood_type, units_donated, status, type, notes, created_at, updated_at) VALUES
(2, 1, '2024-01-15 10:30:00', 'O+', 1.0, 'COMPLETED', 'VOLUNTARY', 'Regular donation, no complications', NOW(), NOW()),
(3, 2, '2024-01-20 14:15:00', 'A+', 1.0, 'COMPLETED', 'VOLUNTARY', 'First-time donor, excellent experience', NOW(), NOW()),
(4, 3, '2024-02-05 09:45:00', 'B+', 1.0, 'COMPLETED', 'EMERGENCY', 'Emergency request fulfilled', NOW(), NOW()),
(5, 1, '2024-02-10 16:20:00', 'AB+', 1.0, 'COMPLETED', 'VOLUNTARY', 'Regular donor, smooth process', NOW(), NOW()),
(6, 4, '2024-02-15 11:00:00', 'O-', 1.0, 'COMPLETED', 'VOLUNTARY', 'Universal donor, high demand', NOW(), NOW());

-- Insert sample feedback
INSERT INTO feedback (user_id, category, rating, subject, message, contact_email, allow_contact, is_anonymous, status, created_at, updated_at) VALUES
(2, 'donation-experience', 5, 'Excellent Service', 'The staff was very professional and the process was smooth. Highly recommend!', 'john.doe@email.com', true, false, 'REVIEWED', NOW(), NOW()),
(3, 'website-usability', 4, 'Great Website', 'Easy to navigate and find blood banks nearby. Could use more filtering options.', 'jane.smith@email.com', true, false, 'PENDING', NOW(), NOW()),
(4, 'blood-search', 5, 'Quick Results', 'Found a blood bank within minutes. The search feature works perfectly.', NULL, false, true, 'RESOLVED', NOW(), NOW()),
(5, 'suggestion', 4, 'Mobile App Suggestion', 'Please consider creating a mobile app for easier access on the go.', 'sarah.williams@email.com', true, false, 'PENDING', NOW(), NOW());