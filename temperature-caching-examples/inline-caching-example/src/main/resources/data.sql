INSERT INTO temperature_sensors (name) VALUES ('A');
INSERT INTO temperature_readings (id, temp_sensor_id, date_time, measurement, scale) VALUES (UUID(), (SELECT id FROM temperature_sensors WHERE name = 'A'), UNIX_TIMESTAMP(), 72.0, 'FAHRENHEIT');
INSERT INTO temperature_readings (id, temp_sensor_id, date_time, measurement, scale) VALUES (UUID(), (SELECT id FROM temperature_sensors WHERE name = 'A'), UNIX_TIMESTAMP(), 0.0, 'CELSIUS');
