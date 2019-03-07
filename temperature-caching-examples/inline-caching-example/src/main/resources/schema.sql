CREATE TABLE IF NOT EXISTS temperature_sensors (
  id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1 INCREMENT BY 1),
  location OTHER,
  name VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS temperature_readings (
  id VARCHAR(256) PRIMARY KEY,
  temp_sensor_id BIGINT,
  date_time BIGINT,
  measurement DECIMAL(10, 2),
  scale VARCHAR(10),
  FOREIGN KEY (temp_sensor_id) REFERENCES temperature_sensors(id)
);
