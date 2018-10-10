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
package example.app.temp.sensor;

import java.util.PrimitiveIterator;
import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import example.app.temp.event.AbstractTemperatureEventPublisher;
import example.app.temp.event.TemperatureEvent;
import example.app.temp.model.Temperature;
import example.app.temp.model.TemperatureReading;
import example.app.temp.service.TemperatureService;

/**
 * The TemperatureSensor class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Service
@SuppressWarnings("unused")
public class TemperatureSensor extends AbstractTemperatureEventPublisher {

	private final PrimitiveIterator.OfInt temperatureStream = new Random(System.currentTimeMillis())
		.ints(-100, 400)
		.iterator();

	private final TemperatureService temperatureService;

	public TemperatureSensor(TemperatureService temperatureService) {

		Assert.notNull(temperatureService, "TemperatureService is required");

		this.temperatureService = temperatureService;
	}

	@Scheduled(initialDelay = 2000L, fixedRateString = "${example.app.temp.sensor.reading.schedule.rate:2000}")
	public void readTemperature() {

		TemperatureReading temperatureReading =
			TemperatureReading.of(Temperature.of(this.temperatureStream.nextInt()));

		publish(new TemperatureEvent(this, this.temperatureService.record(temperatureReading)));
	}
}
