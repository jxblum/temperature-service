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
package example.app.temp.model.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.StreamSupport;

import org.cp.elements.lang.Identifiable;
import org.cp.elements.util.ArrayUtils;
import org.cp.elements.util.CollectionUtils;
import org.springframework.lang.Nullable;

import example.app.temp.model.TemperatureReading;

/**
 * The {@link TemperatureSensorSupport} class is an abstract base class supporting the implementation
 * of temperature sensor devices, encapsulating operations on a {@link Collection}
 * of {@link TemperatureReading TemperatureReadings}.
 *
 * @author John Blum
 * @see java.lang.Iterable
 * @see org.cp.elements.lang.Identifiable
 * @see example.app.temp.model.TemperatureReading
 * @since 1.0.0
 */
@SuppressWarnings({ "unchecked", "unused" })
public abstract class TemperatureSensorSupport implements Identifiable<Long>, Iterable<TemperatureReading> {

  public abstract Collection<TemperatureReading> getTemperatureReadings();

  public <T extends TemperatureSensorSupport> T add(@Nullable TemperatureReading temperatureReading) {

    if (temperatureReading != null) {
      getTemperatureReadings().add(temperatureReading);
    }

    return (T) this;
  }

  @Override @SuppressWarnings("all")
  public Iterator<TemperatureReading> iterator() {
    return Collections.unmodifiableCollection(getTemperatureReadings()).iterator();
  }

  public <T extends TemperatureSensorSupport> T of(@Nullable TemperatureReading... temperatureReadings) {
    return (T) of(Arrays.asList(ArrayUtils.nullSafeArray(temperatureReadings, TemperatureReading.class)));
  }

  public <T extends TemperatureSensorSupport> T of(@Nullable Iterable<TemperatureReading> temperatureReadings) {

    StreamSupport.stream(CollectionUtils.nullSafeIterable(temperatureReadings).spliterator(), false)
      .filter(Objects::nonNull)
      .forEach(this::add);

    return (T) this;
  }

  public int size() {
    return getTemperatureReadings().size();
  }
}
