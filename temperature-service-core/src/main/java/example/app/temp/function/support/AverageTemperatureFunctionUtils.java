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
package example.app.temp.function.support;

import java.util.List;
import java.util.Objects;

import org.cp.elements.lang.MathUtils;

import example.app.temp.model.Temperature;
import example.app.temp.model.TemperatureReading;

/**
 * The AverageTemperatureFunctionUtils class...
 *
 * @author John Blum
 * @since 1.0.0
 */
public abstract class AverageTemperatureFunctionUtils {

	public static Temperature asTemperature(Object functionReturnValue) {
		return Temperature.of(unwrap(functionReturnValue));
	}

	public static TemperatureReading asTemperatureReading(Object functionReturnValue) {
		return TemperatureReading.of(asTemperature(functionReturnValue));
	}

	@SuppressWarnings("unchecked")
	public static double unwrap(Object functionReturnValue) {

		if (functionReturnValue instanceof List) {

			List<Double> temperatures = (List<Double>) functionReturnValue;

			double temperatureSum = temperatures.stream()
				.filter(Objects::nonNull)
				.reduce((temperatureOne, temperatureTwo) -> temperatureOne + temperatureTwo)
				.orElse(0.0d);

			temperatureSum = MathUtils.roundToNearestTenth(temperatureSum);

			functionReturnValue = temperatureSum / temperatures.size();
		}

		return (double) functionReturnValue;
	}
}
