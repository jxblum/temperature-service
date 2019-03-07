CREATE TABLE IF NOT EXISTS TemperatureSensors (
  id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1 INCREMENT BY 1),
  location OTHER,
  name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS TemperatureReadings (
  id VARCHAR PRIMARY KEY DEFAULT TO_CHAR(CURRENT_TIMESTAMP),
  temp_sensor_id BIGINT,
  date_time BIGINT,
  measurement DECIMAL(10, 2),
  scale VARCHAR(10),
  FOREIGN KEY (temp_sensor_id) REFERENCES TemperatureSensors(id)
);