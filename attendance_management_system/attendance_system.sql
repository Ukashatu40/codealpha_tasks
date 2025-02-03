CREATE TABLE admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL
);

CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    date DATE NOT NULL,
    status ENUM('Present', 'Absent') NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

INSERT INTO admin (username, password) VALUES ('admin', 'password123');

INSERT INTO students (name, email, department) VALUES
('John Doe', 'john.doe@example.com', 'Computer Science'),
('Jane Smith', 'jane.smith@example.com', 'Mechanical Engineering'),
('Ali Hassan', 'ali.hassan@example.com', 'Electrical Engineering');

INSERT INTO attendance (student_id, date, status) VALUES
(1, '2025-02-01', 'Present'),
(2, '2025-02-01', 'Absent'),
(3, '2025-02-01', 'Present'),
(1, '2025-02-02', 'Absent'),
(2, '2025-02-02', 'Present');

