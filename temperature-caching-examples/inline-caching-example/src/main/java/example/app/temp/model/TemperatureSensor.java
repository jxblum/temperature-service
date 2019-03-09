/*
 *  Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package example.app.temp.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.datastax.driver.core.DataType.Name;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.geo.Point;

import example.app.temp.model.support.TemperatureSensorSupport;

/**
 * {@link TemperatureSensor} is an Abstract Data Type (ADT) modeling a sensor device that measure physical temperatures.
 *
 * @author John Blum
 * @see javax.persistence.Entity
 * @see javax.persistence.Table
 * @see org.springframework.data.gemfire.mapping.annotation.Region
 * @see example.app.temp.model.support.TemperatureSensorSupport
 * @since 1.0.0
 */
@Data
@Entity
@Region("TemperatureSensors")
@Table(name = "temperature_sensors")
@org.springframework.data.cassandra.core.mapping.Table("temperature_sensors")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(staticName = "create")
@ToString
@SuppressWarnings("unused")
public class TemperatureSensor extends TemperatureSensorSupport {

  @Id
  @PrimaryKey
  @javax.persistence.Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "temp_sensor_id")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @org.springframework.data.cassandra.core.mapping.Column("temperature_readings")
  private Collection<TemperatureReading> temperatureReadings = new ArrayList<>();

  @CassandraType(type = Name.UDT, userTypeName = "Point")
  private Point location;

  @NonNull
  private String name;

}
