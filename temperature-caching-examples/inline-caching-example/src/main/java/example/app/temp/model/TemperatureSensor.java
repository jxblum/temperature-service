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

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.geo.Point;

import example.app.temp.model.support.TemperatureSensorSupport;

/**
 * The TemperatureSensor class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Data
@Entity
@Region("TemperatureSensors")
@Table(name = "TemperatureSensors")
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor(staticName = "create")
@ToString(of = "name")
@SuppressWarnings("unused")
public class TemperatureSensor extends TemperatureSensorSupport {

  @Id
  @javax.persistence.Id
  private Long id;

  @OneToMany
  private Collection<TemperatureReading> temperatureReadings = new ArrayList<>();

  private Point location;

  @NonNull
  private final String name;

}
