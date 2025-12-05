-- Admin table for authentication
CREATE TABLE IF NOT EXISTS admin (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert a sample admin user (password is 'admin123' hashed with BCrypt)
-- You can generate BCrypt hash at: https://bcrypt-generator.com/
INSERT INTO admin (email, password) VALUES 
('admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7I/8X5dJz.2khkmKWLvJ5H7bqxQJTOy')
ON CONFLICT (email) DO NOTHING;
