/* 
 * E-Commerce Backend Platform - Demo Data Script
 * This script seeds the database with a professional set of users and products
 * for testing and demonstration purposes.
 */

-- Ensure we are using the correct database
USE ecommerce_db;

-- Temporarily disable constraints to reset data
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
TRUNCATE TABLE cart_items;
TRUNCATE TABLE carts;
TRUNCATE TABLE products;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- Seed Users 
-- Note: Passwords are BCrypt hashed for 'password123'
INSERT INTO users (name, email, password, role) VALUES 
('System Admin', 'admin@ecommerce.com', '$2a$10$8.UnVuG9HHgffUDAlk8qn.6nQH2ptzNo0PKx67.0SqzW.xuUvCyEq', 'ROLE_ADMIN'),
('Abhijit Behera', 'avjeet111@gmail.com', '$2a$10$8.UnVuG9HHgffUDAlk8qn.6nQH2ptzNo0PKx67.0SqzW.xuUvCyEq', 'ROLE_CUSTOMER'),
('Virat Kohli', 'virat18@gmail.com', '$2a$10$8.UnVuG9HHgffUDAlk8qn.6nQH2ptzNo0PKx67.0SqzW.xuUvCyEq', 'ROLE_CUSTOMER');

-- Seed Product Catalog
-- Real-world products across multiple categories
INSERT INTO products (name, description, price, stock, category, image_url, rating) VALUES
('iPhone 15 Pro', 'Titanium design, A17 Pro chip, powerful Pro camera system.', 999.00, 45, 'Electronics', './docs/products/iphone_15_pro.jpg', 4.9),
('MacBook Air M2', '13.6-inch Liquid Retina display, MacBook Air with M2 chip.', 1099.00, 15, 'Electronics', './docs/products/macbook_air_m2.jpg', 4.8),
('Sony WH-1000XM5', 'Premium noise-canceling headphones with exceptional sound.', 348.00, 80, 'Audio', './docs/products/sony_wh_1000xm5.jpg', 4.7),
('Nike Air Jordan 1 Low', 'The Air Jordan 1 Low updates the classic sneaker with fresh colors.', 125.00, 25, 'Fashion', './docs/products/Nike_Air_Jordan_1_Low.jpg', 4.6),
('Steelcase Gesture', 'Top-rated ergonomic office chair designed for multi-device posture.', 1350.00, 10, 'Office', './docs/products/Steelcase_Gesture.avif', 4.9),
('Logitech MX Master 3S', 'Performance wireless mouse with quiet clicks and 8K DPI sensor.', 99.00, 120, 'Accessories', './docs/products/Logitech_MX_Master_3S.avif', 4.8);

-- Pre-initialize Carts for seeded users
-- Every user must have exactly one cart
INSERT INTO carts (user_id, total_price) VALUES 
(1, 0.0), -- Admin cart
(2, 0.0), -- Abhijit's cart
(3, 0.0); -- Virat's cart

-- Add some products to Alice's cart for testing
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES
(2, 1, 1), -- iPhone 15 Pro
(2, 6, 1); -- Logitech Mouse

-- Update Alice's cart total accordingly
UPDATE carts SET total_price = 1098.00 WHERE id = 2;
