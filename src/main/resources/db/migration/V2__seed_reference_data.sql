INSERT INTO doctors (id, full_name, specialty, phone, email)
VALUES
    (1, 'Dra. María González', 'Cardiología', '555-1001', 'maria.gonzalez@medisalud.com'),
    (2, 'Dr. Carlos Ruiz', 'Pediatría', '555-1002', 'carlos.ruiz@medisalud.com'),
    (3, 'Dra. Ana López', 'Dermatología', '555-1003', 'ana.lopez@medisalud.com');

ALTER TABLE doctors ALTER COLUMN id RESTART WITH 4;
