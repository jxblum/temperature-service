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
package example.app.temp.function.impl;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.data.gemfire.function.annotation.RegionData;
import org.springframework.stereotype.Component;

import example.app.temp.model.Temperature;
import example.app.temp.model.TemperatureReading;

/**
 * The AverageTemperatureFunction class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Component
@SuppressWarnings("unused")
public class AverageTemperatureFunction {

	@GemfireFunction
	public Double averageTemperature(@RegionData Map<String, TemperatureReading> temperatureReadings) {

		Optional<Double> temperatureSum = temperatureReadings.values().stream()
			.map(TemperatureReading::getTemperature)
			.map(Temperature::getMeasurement)
			.reduce((temperatureOne, temperatureTwo) -> temperatureOne + temperatureTwo);

		return temperatureSum.map(sum -> sum / temperatureReadings.size()).orElse(0.0d);
	}
}
