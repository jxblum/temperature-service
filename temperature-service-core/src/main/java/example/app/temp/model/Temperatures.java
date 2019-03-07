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

import java.util.Arrays;
import java.util.Optional;

/**
 * An enumeration of named {@link Temperature} objects.
 *
 * @author John Blum
 * @see example.app.temp.model.Temperature
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public enum Temperatures {

	BOILING_POINT_CELSIUS(Temperature.of(Temperature.Scale.CELSIUS.getBoilingTemperature()).celsius()),
	BOILING_POINT_FAHRENHEIT(Temperature.of(Temperature.Scale.FAHRENHEIT.getBoilingTemperature()).fahrenheit()),
	BOILING_POINT_KELVIN(Temperature.of(Temperature.Scale.KELVIN.getBoilingTemperature()).fahrenheit()),

	FREEZING_POINT_CELSIUS(Temperature.of(Temperature.Scale.CELSIUS.getFreezingTemperature()).fahrenheit()),
	FREEZING_POINT_FAHRENHEIT(Temperature.of(Temperature.Scale.FAHRENHEIT.getFreezingTemperature()).fahrenheit()),
	FREEZING_POINT_KELVIN(Temperature.of(Temperature.Scale.KELVIN.getFreezingTemperature()).fahrenheit()),

	ROOM_TEMPERATURE_FAHRENHEIT(Temperature.of(72).fahrenheit());

	public static Optional<Temperatures> valueOf(Temperature temperature) {

		return Arrays.stream(values())
			.filter(temp -> temp.getTemperature().equals(temperature))
			.findFirst();
	}

	private final Temperature temperature;

	Temperatures(Temperature temperature) {
		this.temperature = temperature;
	}

	public Temperature getTemperature() {
		return this.temperature;
	}

	public TemperatureReading asReading() {
		return TemperatureReading.of(getTemperature());
	}

	@Override
	public String toString() {
		return getTemperature().toString();
	}
}
