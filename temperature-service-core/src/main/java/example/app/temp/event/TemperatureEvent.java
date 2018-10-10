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
package example.app.temp.event;

import java.util.EventObject;

import org.springframework.util.Assert;

import example.app.temp.model.TemperatureReading;

/**
 * The {@link TemperatureEvent} class is an {@link EventObject} type used to track events involving to
 * {@link TemperatureReading TemperatureReadings}.
 *
 * @author John Blum
 * @see java.util.EventObject
 * @see example.app.temp.model.TemperatureReading
 * @since 1.0.0
 */
public class TemperatureEvent extends EventObject {

	private final TemperatureReading temperatureReading;

	public TemperatureEvent(Object source, TemperatureReading temperatureReading) {

		super(source);

		Assert.notNull(temperatureReading, "TemperatureReading is required");

		this.temperatureReading = temperatureReading;
	}

	public TemperatureReading getTemperatureReading() {
		return this.temperatureReading;
	}

	@Override
	public String toString() {

		TemperatureReading temperatureReading = getTemperatureReading();

		return String.format("TEMP [%s] @ [%d ms]",
			temperatureReading.getTemperature(), temperatureReading.getTimestamp());
	}
}
