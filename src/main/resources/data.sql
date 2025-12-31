-- Detta behövs för att Auth Service inte ska krascha
INSERT IGNORE INTO users (username, password, role) VALUES ('admin', 'dummy', 'ADMIN');
INSERT IGNORE INTO users (username, password, role) VALUES ('doctor', 'dummy', 'DOCTOR');
INSERT IGNORE INTO users (username, password, role) VALUES ('patient', 'dummy', 'PATIENT');
INSERT IGNORE INTO users (username, password, role) VALUES ('staff', 'dummy', 'STAFF');
