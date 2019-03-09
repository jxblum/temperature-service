CREATE KEYSPACE IF NOT EXISTS TemperatureService WITH replication = { 'class':'SimpleStrategy', 'replication_factor':1 };
USE TemperatureService;
CREATE TYPE IF NOT EXISTS Point (x DOUBLE, y DOUBLE);
CREATE TYPE IF NOT EXISTS Temperature (measurement DOUBLE, scale TEXT);
CREATE TYPE IF NOT EXISTS TemperatureReading (id UUID, timestamp BIGINT, temperature FROZEN<Temperature>);
CREATE TABLE IF NOT EXISTS temperature_sensors (id BIGINT PRIMARY KEY, location FROZEN<Point>, temperature_readings FROZEN<LIST<TemperatureReading>>, name TEXT);
CREATE INDEX IF NOT EXISTS SensorNameIdx ON temperature_sensors(name);
