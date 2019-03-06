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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.geo.Point;

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
@SuppressWarnings("unused")
public class TemperatureSensor implements Iterable<TemperatureReading> {

  @Id
  @javax.persistence.Id
  private Long id;

  @Transient
  @javax.persistence.Transient
  private Point location;

  @OneToMany
  private Collection<TemperatureReading> temperatureReadings;

  @Override @SuppressWarnings("all")
  public Iterator<TemperatureReading> iterator() {
    return Collections.unmodifiableCollection(this.temperatureReadings).iterator();
  }
}
