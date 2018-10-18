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

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.cp.elements.lang.MathUtils;

import example.app.temp.model.Temperature;
import example.app.temp.model.TemperatureReading;

/**
 * The AverageTemperatureFunctionUtils class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public abstract class AverageTemperatureFunctionUtils {

	public static Temperature asTemperature(Object functionReturnValue) {
		return Temperature.of(unwrap(functionReturnValue));
	}

	public static TemperatureReading asTemperatureReading(Object functionReturnValue) {
		return TemperatureReading.of(asTemperature(functionReturnValue));
	}

	public static Optional<Double> extract(Iterable<Optional<Double>> functionResult) {
		return isNotEmpty(functionResult) ? functionResult.iterator().next() : Optional.empty();
	}

	private static boolean isNotEmpty(Iterable<?> iterable) {
		return iterable != null && iterable.iterator().hasNext();
	}

	@SuppressWarnings("unchecked")
	public static double unwrap(Object functionReturnValue) {

		if (functionReturnValue instanceof Iterable) {

			Iterable<Double> temperatures = (Iterable<Double>) functionReturnValue;

			double averageTemperature = StreamSupport.stream(temperatures.spliterator(), false)
				.filter(Objects::nonNull)
				.collect(Collectors.averagingDouble(temperature -> temperature));

			averageTemperature = MathUtils.roundToNearestTenth(averageTemperature);

			functionReturnValue = averageTemperature;
		}

		return (double) functionReturnValue;
	}
}
